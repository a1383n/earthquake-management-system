<?php


class Data
{
    /**
     * Get XML file from Iranian Seismological Center
     * @return SimpleXMLElement|string
     */
    public static function GetXML(){
        $curl = curl_init("http://irsc.ut.ac.ir/events_list_fa.xml");
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($curl, CURLOPT_HEADER, false);
        curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
        $result = curl_exec($curl);
        curl_close($curl);

        $en = array("0","1","2","3","4","5","6","7","8","9");
        $fa = array("۰","۱","۲","۳","۴","۵","۶","۷","۸","۹");
        $gps = array("N","E");

        $result = str_replace($fa,$en,$result);
        $result = str_replace($gps,'',$result);

        return simplexml_load_string($result);
    }

    public static function GetXMLen(){

    }

    /**
     * Convert XML format to JSON
     * @param SimpleXMLElement $xml
     * @return false|string
     */
    public static function toJSON(SimpleXMLElement $xml){
        $json = json_decode(json_encode($xml));
        unset($json->item[0]);
        return array_values($json->item);
    }
}