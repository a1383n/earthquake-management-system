<?php
require_once __DIR__ . '/vendor/autoload.php';

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;
use Illuminate\Database\Capsule\Manager as Capsule;


//Slim setup
$app = AppFactory::create();

//DB setup
$db = new Capsule;
$db->addConnection([
    'driver' => 'mysql',
    'host' => 'localhost',
    'database' => 'eq',
    'username' => 'root',
    'password' => '',
    'charset' => 'utf8',
    'collation' => 'utf8_unicode_ci',
    'prefix' => '',
]);
$db->setAsGlobal();

//Setup defines
define("MAX_DATA_LIMIT", 100);

#App Start
// Get dates routes
$app->get('/earthquakes', function (Request $request, Response $response) {
    // get and set limit
    $limit = (isset($request->getQueryParams()["limit"])) ? (int)$request->getQueryParams()["limit"] : 20;
    if ($limit > MAX_DATA_LIMIT) {
        $response->getBody()->write(json_encode([
            'ok' => false,
            'des' => 'Maximum allowable limit is ' . MAX_DATA_LIMIT
        ]));
        return $response->withStatus(406)->withHeader('Content-Type', 'application/json');
    }

    //Create base query
    $result = Capsule::table("earthquakes")
        ->limit($limit)
        ->orderBy('id', 'desc');

    //Add filter
    # Date filter
    $year = (isset($request->getQueryParams()["year"])) ? (int)$request->getQueryParams()["year"] : null;
    if ($year) {
        $month = (isset($request->getQueryParams()["month"])) ? $request->getQueryParams()["month"] : null;
        $day = (isset($request->getQueryParams()["day"])) ? $request->getQueryParams()["day"] : null;

        if ($month && $day) {
            $result->where('date', 'like', '%' . $year . '/' . $month . '/' . $day . '%');
        } elseif ($month) {
            $result->where('date', 'like', '%' . $year . '/' . $month . '%');
        } else {
            $result->where('date', 'like', '%' . $year . '%');
        }
    }

    # Location filter
    $state = (isset($request->getQueryParams()["state"])) ? $request->getQueryParams()["state"] : null;
    if ($state) {
        $city = (isset($request->getQueryParams()["city"])) ? $request->getQueryParams()["city"] : null;
        if ($city) {
            $result->where('reg1', 'like', '%' . $state . '%')->where('reg1', 'like', '%' . $city . '%');
        } else {
            $result->where('reg1', 'like', '%' . $state . '%');
        }
    }

    //Run query
    $result = $result->get();
    $result = Converter::toList($result);

    $response->getBody()->write(json_encode($result));
    return $response->withHeader('Content-Type', 'application/json');
});
$app->get("/earthquakes/{id}", function (Request $request, Response $response, $args) {
    $result = Capsule::table("earthquakes")->find($args['id']);

    $result = Converter::toSingleModel($result);

    if (!$result) {
        $response->getBody()->write(json_encode([
            'ok' => false,
            'des' => 'Earthquake not found'
        ]));
        return $response->withStatus(404)->withHeader('Content-Type', 'application/json');
    } else {
        $response->getBody()->write(json_encode($result));
    }

    return $response->withHeader('Content-Type', 'application/json');
});

$app->get("/states", function (Request $request, Response $response) {
    $result = Capsule::table('states')->get();
    $response->getBody()->write(json_encode($result));
    return $response->withHeader('Content-Type', 'application/json');
});
$app->get("/states/{id}", function (Request $request, Response $response, $args) {
    $state = Capsule::table('states')->find($args['id']);
    $cities = Capsule::table('cities')->where('state_id', '=', $args['id'])->get();

    $response->getBody()->write(json_encode([
        'state' => $state,
        'cities' => $cities
    ]));
    return $response->withHeader('Content-Type', 'application/json');
});

//Sync routes
$app->get('/sync', function (Request $request, Response $response) {
    $data = Data::toJSON(Data::GetXML());
    $i = 0;
    foreach ($data as $row) {
        if (empty(Capsule::table('earthquakes')->find($row->id))) {
            $i++;
            Capsule::table('earthquakes')->insert([
                'id' => $row->id,
                'reg1' => $row->reg1,
                'reg2' => $row->reg2,
                'reg3' => $row->reg3,
                'dis1' => $row->dis1,
                'dis2' => $row->dis2,
                'dis3' => $row->dis3,
                'mag' => $row->mag,
                'dep' => $row->dep,
                'lat' => $row->lat,
                'lon' => $row->long,
                'date' => $row->date
            ]);

            $row->reg1 = Converter::location($row->reg1);

            if (Capsule::table('states')->where('fa_title', '=', $row->reg1['state'])->get()->count() == 0) {
                Capsule::table('states')->insert([
                    'fa_title' => $row->reg1['state']
                ]);
            }
            if (Capsule::table('cities')->where('fa_title', '=', $row->reg1['city'])->get()->count() == 0) {
                Capsule::table('cities')->insert([
                    'state_id' => Capsule::table('states')->where('fa_title', '=', $row->reg1['state'])->get('id')[0]->id,
                    'fa_title' => $row->reg1['city']
                ]);
            }
        }
    }

    $response->getBody()->write(json_encode(['ok' => true, 'total' => Capsule::table('earthquakes')->count(), 'new' => $i]));

    return $response->withHeader('Content-Type', 'application/json');
});


$app->addErrorMiddleware(true, false, false);
$app->run();

