package com.example.fiwareteste;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {

    String token = "02b3a4f3-e489-44b0-8681-6770599eba78";

    /////////////// Devices

    //@Headers({"fiware-service: openiot", "fiware-servicepath: /", "Content-Type: application/json" })
    @Headers({"fiware-service: openiot", "fiware-servicepath: /"})
    @GET("devices")
    Call<JsonObject> getDevices();

    @Headers({"fiware-service: openiot", "fiware-servicepath: /"})
    @GET("entities/{device}?options=keyValues")
    Call<JsonObject> getEntities(@Path("device") String devices,
                                 @Body String comando);

    @Headers({"fiware-service: openiot", "fiware-servicepath: /"})
    @GET("services")
    Call<JsonObject> getServices(@Body String services);

    @Headers({"fiware-service: openiot", "fiware-servicepath: /", "Content-Type: application/json"})
    @POST("devices")
    Call<JsonObject> createDevice(@Body JsonObject jsonObject);


    @Headers({"fiware-service: openiot", "fiware-servicepath: /"})
    @DELETE("devices/{device}")
    Call<JsonObject> deleteDevice(@Path("device") String device);

    @Headers({"fiware-service: openiot", "fiware-servicepath: /", "Content-Type: application/json"})
    @PUT("devices/{device}")
    Call<JsonObject> updateDevice(@Path("device") String device,
                                  @Body JsonObject jsonObject);


    //////////////// Subscriptions

    @Headers({"content-type: application/json"})
    @POST("subscriptions")
    Call<JsonObject> setSubscription(@Body String subscription);

    @POST("subscriptions")
    Call<JsonObject> listSubscriptions(@Body String subscription);


    /////////////// Usuarios

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @POST("v1/users")
    Call<JsonObject> createUser(@Body JsonObject usuario);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @GET("users/{userId}")
    Call<JsonObject> getUser(@Path("userId") String usuarioId);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @GET("v1/users")
    Call<JsonObject> getUsers();

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @PATCH("v1/users/{userId}")
    Call<JsonObject> updateUser(@Path("userId") String usuarioId,
                                @Body JsonObject jsonObject);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @DELETE("v1/users/{userId}")
    Call<JsonObject> deleteUser(@Path("userId") String usuarioId);


    //////////////// Organizações

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @POST("organizations")
    Call<JsonObject> createOrganization(@Body String organizacao);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @GET("organizations")
    Call<JsonObject> getOrganizations(@Body String organizacao);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @PATCH("organizations/{organization-id}")
    Call<JsonObject> updateUOrganizacao(@Path("organizationId") String organizationId,
                                @Body String organizacao);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @DELETE("organizations/{organization-id}")
    Call<JsonObject> deleteUOrganizacao(@Path("organizationId") String organizationId);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @PUT("organizations/{organization-id}/users/{userId}/organization_roles/member")
    Call<JsonObject> createMemberOrganizacao(@Path("organizationId") String organizationId,
                                        @Path("userId") String usuarioId);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @PUT("organizations/{organization-id}/users/{userId}/organization_roles/owner")
    Call<JsonObject> createOwnerOrganizacao(@Path("organizationId") String organizationId,
                                             @Path("userId") String usuarioId);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @GET("organizations/{organization-id}/users")
    Call<JsonObject> getUsersOrganizacao(@Path("organizationId") String organizationId);

    @Headers({"content-type: application/json", "X-Auth-token: "+ token})
    @DELETE("organizations/{organization-id}")
    Call<JsonObject> deleteUserOrganizacao(@Path("organizationId") String organizationId,
                                        @Path("userId") String usuarioId);


    ///////////////////// Consulta Banco de DadoS

    @Headers({"Content-type: application/json"})
    @POST("_sql?format=json")
    Call<JsonObject> getValues(@Body JsonObject jsonObject);


    ///////////////////// Permissões

    @Headers({"content-type: application/json", "Accept: application/json"})
    @POST("v1/auth/tokens")
    Call<JsonObject> getPermission(@Body JsonObject jsonObject);

}
