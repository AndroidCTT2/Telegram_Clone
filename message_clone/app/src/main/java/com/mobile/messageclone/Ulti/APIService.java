package com.mobile.messageclone.Ulti;

import com.mobile.messageclone.Model.MyResponse;
import com.mobile.messageclone.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAmYeL9Fc:APA91bG97pDR1dW_hJbVPqPMqgRCQomEzfA81klg43hIUxFVVhlAigmYt6WbVnnk52E082N_5qWacgqBCm4rvrljNY1ft0bqOfgHTu2gsNkZWpaxy125pGdS0wIqoCAkqUOPb5DjwcKs"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
