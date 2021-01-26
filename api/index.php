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
    $timestamp = (isset($request->getQueryParams()["timestamp"])) ? (int)$request->getQueryParams()["timestamp"] : null;
    if ($timestamp) {
            $result->where('date', 'like', Converter::timestampToJalali($timestamp) . '%');
    }

    # Location filter
    $province = (isset($request->getQueryParams()["province"])) ? $request->getQueryParams()["province"] : null;
    if ($province) {
        if ($province <= Capsule::table('provinces')->count()) {
            $region = (isset($request->getQueryParams()["region"])) ? $request->getQueryParams()["region"] : null;
            $province = Capsule::table('provinces')->find($province)->fa_title;
            if ($region) {
                $region = Capsule::table('regions')->find($region)->fa_title;
                $result->where('reg1', 'like', '%' . $province . '%')->where('reg1', 'like', '%' . $region . '%');
            } else {
                $result->where('reg1', 'like', '%' . $province . '%');
            }
        }else{
            $response->getBody()->write(json_encode([
                'ok'=>false,
                'des'=>'Province not found with this id'
            ]));
            return $response->withHeader('Content-Type', 'application/json');
        }
    }

    $page = (isset($request->getQueryParams()["page"])) ? $request->getQueryParams()["page"] : 1;

    //Set pages config
    $result->paginate(20,['*'],null,$page);

    //Run query
    $result = $result->get();
    $result = Converter::toList($result);

    $response->getBody()->write(json_encode($result));
    return $response->withHeader('Content-Type', 'application/json');
});
$app->get("/earthquakes/{id}", function (Request $request, Response $response, $args) {
    $result = Capsule::table("earthquakes")->find($args['id']);

    if (!$result) {
        $response->getBody()->write(json_encode([
            'ok' => false,
            'des' => 'Earthquake not found'
        ]));
        return $response->withStatus(404)->withHeader('Content-Type', 'application/json');
    } else {
        $result = Converter::toSingleModel($result);
        $response->getBody()->write(json_encode($result));
    }

    return $response->withHeader('Content-Type', 'application/json');
});

$app->get("/provinces", function (Request $request, Response $response) {
    $result = Capsule::table('provinces')->get();
    $response->getBody()->write(json_encode($result));
    return $response->withHeader('Content-Type', 'application/json');
});
$app->get("/provinces/{id}", function (Request $request, Response $response, $args) {
    $state = Capsule::table('provinces')->find($args['id']);
    $cities = Capsule::table('regions')->where('province_id', '=', $args['id'])->get();

    $response->getBody()->write(json_encode([
        'province' => $state,
        'regions' => $cities
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

            if (Capsule::table('provinces')->where('fa_title', '=', $row->reg1[0])->get()->count() == 0) {
                Capsule::table('provinces')->insert([
                    'fa_title' => $row->reg1[0]
                ]);
            }
            if (Capsule::table('regions')->where('fa_title', '=', $row->reg1[1])->get()->count() == 0) {
                Capsule::table('regions')->insert([
                    'province_id' => Capsule::table('provinces')->where('fa_title', '=', $row->reg1[0])->get('id')[0]->id,
                    'fa_title' => $row->reg1[1]
                ]);
            }
        }
    }

    $response->getBody()->write(json_encode(['ok' => true, 'total' => Capsule::table('earthquakes')->count(), 'new' => $i]));

    return $response->withHeader('Content-Type', 'application/json');
});


$app->addErrorMiddleware(true, false, false);
$app->run();

