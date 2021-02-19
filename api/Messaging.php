<?php

use Kreait\Firebase\Factory;
use Kreait\Firebase\Messaging\CloudMessage;
use Kreait\Firebase\Messaging\Notification;
use Kreait\Firebase\Messaging\AndroidConfig;

class Messaging
{
    /**
     * @var \Kreait\Firebase\Messaging
     */
    public $messagingService;

    private $testDebugPhone = "d_jLRwn4QUKv6T3t8uU-zA:APA91bEiMlUvH2fKfzmc_oA2QybOBWsvJtUd08p7chP7oK28QqeiNN1pAfB_dso2RURNCbwm1D6AlR10cpSOeZuOruCprwKNBbKY1gx5kq-hatNtxPGGxWHmfA_QU88HKSTjAAquVh-6";

    /**
     * Messaging constructor.
     */
    public function __construct()
    {
        // Authentication for Firebase service
        $this->messagingService = (new Factory())->withServiceAccount(__DIR__ . "/private/firebase-admin-sdk-key.json")->createMessaging();
    }

    /**
     * @return \Kreait\Firebase\Messaging
     */
    public function getMessagingService()
    {
        return $this->messagingService;
    }

    /**
     * Send Message to android devices
     * @param Earthquake $earthquake
     */
    public function sendMessage(Earthquake $earthquake)
    {
        $province = $earthquake->province;
        $region = $earthquake->region;

        $fa_text = "زمین لرزه ای به بزرگی " . $earthquake->mag . " ریشتر " . $province->fa_title . "," . $region->fa_title . " لرزاند";
        $en_text = "An earthquake measuring $earthquake->mag on the Richter scale shook the $province->en_title,$region->en_title";


        $notification = Notification::create($fa_text);


        $message = CloudMessage::withTarget("condition",$this->generateConditionString($earthquake))
            ->withNotification($notification)//     ->withAndroidConfig($androidConfig)
        ;


        $this->messagingService->send($message);
    }


    /**
     * Create string for topic @example fa_2_all
     * @param $lang
     * @param $mag
     * @param $en_province
     * @return string
     */
    private function generateTopicString($lang, $mag, $en_province)
    {
        // fa_3_tehran Ex.

        return $lang . "_" . $mag . "_" . strtolower(($en_province == null) ? "all" : $en_province);
    }

    /**
     * Create Condition string for FCM
     * @param Earthquake $earthquake
     * @return string
     */
    private function generateConditionString(Earthquake $earthquake)
    {
        $mag = (double)$earthquake->mag;

        if ($mag > 7.0 /* 7 */) {
            $mag = 7;
        } else if ($mag <= 6.9 && $mag > 5.9 /* 6 */) {
            $mag = 6;
        } else if ($mag <= 5.9 && $mag > 4.9 /* 5 */) {
            $mag = 5;
        } else if ($mag <= 4.9 && $mag > 3.9 /* 4 */) {
            $mag = 4;
        } else if ($mag <= 3.9 && $mag > 2.9 /* 3 */) {
            $mag = 3;
        } else if ($mag <= 2.9 && $mag > 1.9 /* 2 */) {
            $mag = 2;
        } else {
            $mag = "Unknown";
        }


        $string =
            "'TopicA' in topics && ('" . $this->generateTopicString("fa", $mag, $earthquake->province->en_title) . "'" . " in " . "topics" . " || ".
            "'" . $this->generateTopicString("fa", "all", $earthquake->province->en_title) . "'" . " in " . "topics" . " || " .
            "'" . $this->generateTopicString("fa", "all", "all") . "'" . " in " . "topics)" ;



        return $string;
    }

}