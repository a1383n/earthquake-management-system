<?php

use Illuminate\Database\Capsule\Manager as Capsule;

class Region {
    public $id; //int
    public $fa_title; //String
    public $en_title; //String

    /**
     * Region constructor.
     * @param $id
     * @param $fa_title
     * @param $en_title
     */
    public function __construct($id, $fa_title, $en_title)
    {
        $this->id = $id;
        $this->fa_title = $fa_title;
        $this->en_title = $en_title;
    }


}
class Province {
    public $id; //int
    public $fa_title; //String
    public $en_title; //String

    /**
     * Province constructor.
     * @param $id
     * @param $fa_title
     * @param $en_title
     */
    public function __construct($id, $fa_title, $en_title)
    {
        $this->id = $id;
        $this->fa_title = $fa_title;
        $this->en_title = $en_title;
    }


}
class Earthquake {

    private static string $table_name = "earthquakes";

    public $id; //int
    public $region; //Region
    public $province; //Province
    public $lat; //double
    public $long; //double
    public $mag; //double
    public $dep; //int
    public $timestamp; //int

    /**
     * Earthquake constructor.
     * @param $id
     * @param Region $region
     * @param Province $province
     * @param $lat
     * @param $long
     * @param $mag
     * @param $dep
     * @param $timestamp
     */
    public function __construct($id,Region $region,Province $province, $lat, $long, $mag, $dep, $timestamp)
    {
        $this->id = $id;
        $this->region = $region;
        $this->province = $province;
        $this->lat = $lat;
        $this->long = $long;
        $this->mag = $mag;
        $this->dep = $dep;
        $this->timestamp = $timestamp;
    }
}