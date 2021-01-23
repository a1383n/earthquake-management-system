<?php
require_once __DIR__ . '/vendor/autoload.php';

class Converter
{
    /**
     * Convert String location to array
     * @param $string
     * @return array
     */
    public static function location($string){
        $array =  explode("،",$string,2);
        return [
            'state'=>trim($array[1]),
            'city'=>trim($array[0])
        ];
    }

    /**
     * Convert String date to array
     * @param $string
     * @return array[]
     */
    public static function date($string){
        $array = explode(" ",$string,2);

        $date = explode("/",$array[0],3);
        $time = explode(":",$array[1],3);

        return [
            'date'=>[
                'year'=>$date[0],
                'month'=>$date[1],
                'day'=>$date[2]
            ],
            'time'=>[
                'hour'=>$time[0],
                'second'=>$time[1],
                'millisecond'=>$time[2]
            ]
        ];
    }

    public static function autoConvert($result){
        for ($i = 0;$i < $result->count();$i++){
            $result[$i]->reg1 = Converter::location($result[$i]->reg1);
            $result[$i]->reg2 = Converter::location($result[$i]->reg2);
            $result[$i]->reg3 = Converter::location($result[$i]->reg3);

            $result[$i]->date = Converter::date($result[$i]->date);

        }

        return $result;
    }
}