<?php

use Illuminate\Database\Capsule\Manager as Capsule;

class Converter
{
    /**
     * Convert String location to array
     * @param $string
     * @return array
     */
    public static function location($string)
    {
        $array = explode("،", $string, 2);
        return [
            trim($array[1]),
            trim($array[0])
        ];
    }

    /**
     * Convert String date to array
     * @param $string
     * @return false|int
     */
    public static function date($string)
    {
        $array = explode(" ", $string, 2);

        $date = explode("/", $array[0], 3);
        $time = explode(":", $array[1], 3);

        return jmktime($time[0], $time[1], round($time[2]), $date[1], $date[2], $date[0]);
    }

    public static function toList($result)
    {
        $short = array();
        for ($i = 0; $i < sizeof($result); $i++) {
            $data = $result[$i];
            $province = Capsule::table('provinces')->where('fa_title', '=', self::location($data->reg1)[0])->get()[0];
            $region = Capsule::table('regions')->where('fa_title','=',self::location($data->reg1)[1])->get()[0];

            array_push($short, [
                'id' => $data->id,
                'region' => [
                    'id'=>$region->id,
                    'fa_title'=>$region->fa_title,
                    'en_title'=>$region->en_title
                ],
                'province'=>[
                    'id' => $province->id,
                    'fa_title' => $province->fa_title,
                    'en_title' => $province->en_title
                ],
                'lat'=>(double) $data->lat,
                'long'=> (double) $data->lon,
                'mag'=>(double) $data->mag,
                'dep' =>(int) $data->dep,
                'timestamp'=>self::date($data->date)
            ]);
        }

        return $short;
    }

    public static function toSingleModel($result){
        $province = Capsule::table('provinces')->where('fa_title', '=', self::location($result->reg1)[0])->get()[0];
        $region = Capsule::table('regions')->where('fa_title','=',self::location($result->reg1)[1])->get()[0];

        return [
            'id' => $result->id,
            'region' => [
                'id'=>$region->id,
                'fa_title'=>$region->fa_title,
                'en_title'=>$region->en_title
            ],
            'province'=>[
                'id' => $result->id,
                'fa_title' => $province->fa_title,
                'en_title' => $province->en_title
            ],
            'lat'=>(double) $result->lat,
            'long'=> (double) $result->lon,
            'mag'=>(double) $result->mag,
            'dep' =>(int) $result->dep,
            'timestamp'=>self::date($result->date)
        ];
    }

    public static function timestampToJalali($timestamp){
        return jdate("Y/m/d",$timestamp,'','','en');
    }
}