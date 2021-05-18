package com.example.thelastdeath.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.thelastdeath.entity.AppExercise;
import com.example.thelastdeath.entity.AppProgramType;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.UserProgram;
import com.example.thelastdeath.entity.UserProgramExercise;
import com.example.thelastdeath.entity.UserProgramSession;
import com.example.thelastdeath.entity.UserStats;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.entity.helper.MyJsonArrayRequest;
import com.example.thelastdeath.entity.helper.MyJsonObjectRequest;
import com.example.thelastdeath.entity.helper.VolleyErrorParser;
import com.example.thelastdeath.queue.MySingletonQueue;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO LIST:
//  - Implement:
//      - /user_program_exercises/
//            - (DONE) PUT
//            - (DONE) DELETE
//            - (DONE) GET
//            - (DONE) POST
//      -
//      - /user_program_sessions/
//            - (DONE) DELETE
//            - (DONE) PUT
//            - (DONE) GET
//            - (DONE) POST
//      - /user_programs/
//            - (DONE) DELETE
//            - (DONE) PUT
//            - (DONE) POST
//            - (DONE) GET (specific rid)
//            - (DONE) GET (all)
//              - Accessible in getUserExpandedchildren(..)
//      - /app_exercises/
//            - (DONE) POST
//            - (DONE) GET (specific rid)
//            - (DONE) GET (all)
//            - (DONE) PUT
//                  - (får ikke hentet respons-message kanskje grunnet differanse i parametre mot tusk.systems (infobox_color_color vs infobox_color)). Derav "Unknown error.." som respons til bruker
//            - (DONE) DELETE
//                  - Får ikke korrekt respons i frontend her heller
//            -
//      - /app_program_types/
//            - (DONE) DELETE
//            - (DONE) GET (all)
//            - (DONE) GET (specific rid)
//            - (DONE) POST
//            - (DONE) PUT
//      - /users/
//            - (DONE) GET/POST/PUT/DELETE
//      - /user_stats/
//             -(DONE) GET
//  - Fix:
//      - apiResponse.message in frontend for PUT/DELETE in '/app_exercises/'

/**
 * @author 
 * @version 1.1
 * Repository for all HTTP-queries against the API
 * @see <a href="https://tusk.systems/trainingapp/v2/api-doc.html">
 */
public class DataRepository {
    // Endpoints
    private static final String BASE_URL_ENDPOINTS = UtilsAPI.API_BASE_URL; // "https://tusk.systems/trainingapp/v2/api.php/";
    private static final String API_KEY = UtilsAPI.API_KEY; // "[*** din API key***]";
    private static final String EXPAND_CHILDREN = "&_expand_children=true";
    private static final String API_KEY_ENDPOINT = "?_api_key=" + UtilsAPI.API_KEY;


    private final String KEY_CLASS_AppProgramType = "CLASS_APP_PROGRAM_TYPE";
    private final String KEY_CLASS_AppExercise = "CLASS_APP_EXERCISE";
    private final String KEY_CLASS_UserProgram = "CLASS_APP_USERPROGRAM";
    private final String KEY_CLASS_UserProgramSession = "CLASS_UserProgramSessions";
    private final String KEY_CLASS_UserProgramExercise = "CLASS_UserProgramExercise";


    private MutableLiveData<ApiError> errorMessage = new MutableLiveData<>();
    private MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    // MyJsonObjectRequest arver fra JsonObjectRequest slik at vi kan overstyre parseNetworkResponse() for å kunne hente ut HTTP responsekode:
    private MyJsonObjectRequest myJsonGetRequest;
    private MyJsonObjectRequest myJsonPostRequest;
    private MyJsonObjectRequest myJsonPutRequest;
    private MyJsonObjectRequest myJsonDeleteRequest;

    private MyJsonArrayRequest myJsonArrayGetRequest;

    // Holder på siste nedlastede objekter
    private User currentUser = null;
    private UserStats currentUserStats = null;
    private List<AppProgramType> currentAppProgramTypeList = null;
    private AppProgramType currentAppProgramType = null;
    private List<AppExercise> currentAppExerciseList = null;
    private AppExercise currentAppExercise = null;
    private UserProgram currentUserProgram = null;
    private UserProgramSession currentUserProgramSession = null;
    private UserProgramExercise currentUserProgramExercise = null;
    private IconFileNames currentIconFileNames = null;

    // Indikerer om nedlasting pågår.
    private boolean downloading = false;

    private RequestQueue queue = null;

    public DataRepository(Application application) {
        // Tom konstruktør
    }

    public MutableLiveData<ApiError> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<ApiResponse> getApiResponse() {
        return apiResponse;
    }


    //<editor-fold desc="/users/">
    // GET user
    public void getUser(Context context, final String firebaseId, boolean forceDownload) {
        Utils.appendLogger("Call-> DataRepository.getUser(..)");


        // Last ned fra server kun dersom User = null eller forceDownload = true:
        if (forceDownload || this.currentUser == null) {
            // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "users/" + firebaseId + "?_api_key=" + API_KEY;
                Utils.appendLogger("mUrlString: " + mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;   //<==
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;  //<==
                                Gson gson = new Gson();
                                currentUser = gson.fromJson(jsonObject.toString(), User.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUser, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;  //<==
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached User", this.currentUser, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    // GET user expandChildren
    public void getUserExpandedchildren(Context context, final String firebaseId, boolean forceDownload) {
        Utils.appendLogger("Call-> DataRepository.getUser(..)");


        // Last ned fra server kun dersom User = null eller forceDownload = true:
        if (forceDownload || this.currentUser == null) {
            // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "users/" + firebaseId + "?_api_key=" + API_KEY + EXPAND_CHILDREN;
                appendUrlString(mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;   //<==
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;  //<==
                                Gson gson = new Gson();
                                currentUser = gson.fromJson(jsonObject.toString(), User.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUser, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;  //<==
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached User", this.currentUser, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    // POST User
    public void postUser(Context context, String firebase_id, String name, String phone, String email, int birth_year) {
        String mUrlString = BASE_URL_ENDPOINTS + "users/";

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("firebase_id", firebase_id);
        params.put("name", name);
        params.put("phone", phone);
        params.put("email", email);
        params.put("birth_year", String.valueOf(birth_year));

        queue = MySingletonQueue.getInstance(context).getRequestQueue();
        myJsonPostRequest = new MyJsonObjectRequest(
                Request.Method.POST,
                mUrlString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userAsJsonObject = response.getJSONObject("record");
                            User user = gson.fromJson(userAsJsonObject.toString(), User.class);
                            ApiResponse resp = new ApiResponse(true, message, user, myJsonPostRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiError apiError = VolleyErrorParser.parse(error);
                        errorMessage.postValue(apiError);
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(myJsonPostRequest);
    }

    // PUT user
    public void putUser(Context context, String firebase_id, String name, String phone, String email, int birth_year) {
        String mUrlString = BASE_URL_ENDPOINTS + "users/";

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("firebase_id", firebase_id);
        params.put("name", name);
        params.put("phone", phone);
        params.put("email", email);
        params.put("birth_year", String.valueOf(birth_year));

        queue = MySingletonQueue.getInstance(context).getRequestQueue();
        myJsonPutRequest = new MyJsonObjectRequest(
                Request.Method.PUT,
                mUrlString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userAsJsonObject = response.getJSONObject("record");
                            User user = gson.fromJson(userAsJsonObject.toString(), User.class);
                            ApiResponse resp = new ApiResponse(true, message, user, myJsonPutRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiError apiError = VolleyErrorParser.parse(error);
                        errorMessage.postValue(apiError);
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(myJsonPutRequest);
    }

    // DELETE user
    public void deleteUser(Context context, String firebase_id) {
        String mUrlString = BASE_URL_ENDPOINTS + "users/" + firebase_id + "?_api_key=" + API_KEY;
        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonDeleteRequest = new MyJsonObjectRequest(
                Request.Method.DELETE,
                mUrlString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResponse resp = new ApiResponse(true, "OK", (User) null, myJsonDeleteRequest.getHttpStatusCode());
                        apiResponse.postValue(resp);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiError apiError = VolleyErrorParser.parse(error);
                        errorMessage.postValue(apiError);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(myJsonDeleteRequest);
    }
    //</editor-fold>

    //<editor-fold desc="/user_stats/">
    // GET user_stats
    public void getUserStats(Context context, final String firebaseId, boolean forceDownload) {
        Utils.appendLogger("Call-> DataRepository.getUserStats(..)");

        // Last ned fra server kun dersom UserStats = null eller forceDownload = true
        if (forceDownload || this.currentUserStats == null) {
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "user_stats/" + firebaseId + "?_api_key=" + API_KEY + EXPAND_CHILDREN;
                Utils.appendLogger("mUrlString: " + mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;
                                Gson gson = new Gson();
                                currentUserStats = gson.fromJson(jsonObject.toString(), UserStats.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUserStats, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached UserStats", this.currentUserStats, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }
    //</editor-fold>

    //<editor-fold desc="/app_program_types/">

    /**
     * GET (ALL) app_program_types
     */
    public void getAllAppProgramTypes(Context context, boolean forceDownload) {
        // TODO: Her er det kanskje jsonArray
        Utils.appendLogger("Call-> DataRepository.getAllAppProgramTypes(..)");

        // Last ned fra server kun dersom AppProgramTypes = null eller forceDownload = true
        if (forceDownload || this.currentAppProgramTypeList == null) {
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "app_program_types/"/* + firebaseId*/ + API_KEY_ENDPOINT + EXPAND_CHILDREN;
                Utils.appendLogger("mUrlString: " + mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;
                myJsonArrayGetRequest = new MyJsonArrayRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                downloading = false;
                                try {
                                    Gson gson = new Gson();

                                    // Loop gjennom returnert JASON-array
                                    ArrayList<AppProgramType> tmpList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject appProgramTypeJson = jsonArray.getJSONObject(i);
                                        AppProgramType appProgramType = gson.fromJson(appProgramTypeJson.toString(), AppProgramType.class);
                                        tmpList.add(appProgramType);
                                    }
                                    currentAppProgramTypeList = tmpList;
                                    ApiResponse resp = new ApiResponse(true, "OK", currentAppProgramTypeList, myJsonArrayGetRequest.getHttpStatusCode());
                                    apiResponse.postValue(resp);
                                } catch (JSONException e) {
                                    Utils.appendLogger(e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonArrayGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached List<AppProgramType>", this.currentUserStats, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    /**
     * GET (specific rid) app_program_types
     *
     * @param rid Given rid
     */
    public void getAppProgramTypeFromRid(Context context, String rid, boolean forceDownload) {
        // /app_program_types/[rid]?_api_key=[din-api-key]
        appendCall("getAppProgramTypeFromRid");
        if (forceDownload || this.currentAppProgramType == null) {
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "app_program_types/" + rid + API_KEY_ENDPOINT;
                appendUrlString(mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;
                                Gson gson = new Gson();
                                currentAppProgramType = gson.fromJson(jsonObject.toString(), AppProgramType.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentAppProgramType, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached AppProgramType", this.currentAppProgramType, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    /**
     * POST app_program_types
     *
     * @param description Required
     * @param icon        NOT Required (E.g. "myNiceIcon.png")
     * @param back_color  NOT Required (E.g. "#FE0980")
     */
    public void postAppProgramType(Context context, String description, String icon, String back_color) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("description", description);
        params.put("icon", icon);
        params.put("back_color", back_color);

        POST_GivenObject(context, "/app_program_types/", params, KEY_CLASS_AppProgramType);
    }

    /**
     * PUT app_program_types
     */
    public void putAppProgramType(Context context, String rid, String description, String icon, String back_color) {
        appendCall("putAppProgramType");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("description", description);
        params.put("icon", icon);
        params.put("back_color", back_color);

        PUT_GivenObject(context, "/app_program_types/", params, KEY_CLASS_AppProgramType);
    }

    public void deleteAppProgramType(Context context, String rid) {
        DELETE_GivenObject(context, "/app_program_types/", KEY_CLASS_AppProgramType, rid + API_KEY_ENDPOINT);
    }
    //</editor-fold>

    //<editor-fold desc="/app_exercises/">
    public void getAllAppExercises(Context context, boolean forceDownload) {
        appendCall("getAllAppExercises");

        if (forceDownload || this.currentAppExerciseList == null) {
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "/app_exercises/" + API_KEY_ENDPOINT;
                Utils.appendLogger("mUrlString: " + mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;
                myJsonArrayGetRequest = new MyJsonArrayRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                downloading = false;
                                try {
                                    Gson gson = new Gson();

                                    // Loop gjennom returnert JASON-array
                                    ArrayList<AppExercise> tmpList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject appExerciseJson = jsonArray.getJSONObject(i);
                                        AppExercise appExercise = gson.fromJson(appExerciseJson.toString(), AppExercise.class);
                                        tmpList.add(appExercise);
                                    }
                                    currentAppExerciseList = tmpList;
                                    ApiResponse resp = new ApiResponse(true, "OK", currentAppExerciseList, myJsonArrayGetRequest.getHttpStatusCode(), "");
                                    apiResponse.postValue(resp);
                                } catch (JSONException e) {
                                    Utils.appendLogger(e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonArrayGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached List<AppExercise>", this.currentAppExerciseList, myJsonGetRequest.getHttpStatusCode(), "");
            apiResponse.postValue(resp);
        }
    }

    public void getAppExerciseFromRid(Context context, String rid, boolean forceDownload) {
        // /app_exercises/[rid]?_api_key=[din-api-key]
        appendCall("getAppExerciseFromRid");
        if (forceDownload || this.currentAppExercise == null) {
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "/app_exercises/" + rid + API_KEY_ENDPOINT;
                appendUrlString(mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;
                                Gson gson = new Gson();
                                currentAppExercise = gson.fromJson(jsonObject.toString(), AppExercise.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentAppExercise, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached AppExercise", this.currentAppExercise, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    public void postAppExercise(Context context, String name, String description, String icon, String infobox_color) {
        // Det ser ut til at det er en feil i API-et som gjør at 'infobox_color_color' ikke settes.
        appendCall("postAppExercise");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("name", name);
        params.put("description", description);
        params.put("icon", icon);
        params.put("infobox_color", infobox_color); // TODO: her er det endret på infobox_color fra infobox_color_color

        POST_GivenObject(context, "/app_exercises/", params, KEY_CLASS_AppExercise);
    }


    public void putAppExercise(Context context, String rid, String name, String description, String icon, String infobox_color) {
        appendCall("putAppExercise");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("name", name);
        params.put("description", description);
        params.put("icon", icon);
        params.put("infobox_color", infobox_color); // TODO: her er det endret på infobox_color fra infobox_color_color

        PUT_GivenObject(context, "/app_exercises/", params, KEY_CLASS_AppExercise);
    }

    public void deleteAppExercise(Context context, String rid) {
        appendCall("deleteAppExercise");
        DELETE_GivenObject(context, "/app_exercises/", KEY_CLASS_AppExercise, rid + API_KEY_ENDPOINT);
    }
    //</editor-fold>

    //<editor-fold desc="/user_programs/">
    public void getUserProgram(Context context, String rid, boolean forceDownload) {
        appendCall("getUserProgram");

        if (forceDownload || this.currentUserProgram == null) {
            // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "/user_programs/" + rid + "?_api_key=" + API_KEY;
                Utils.appendLogger("mUrlString: " + mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;   //<==
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;  //<==
                                Gson gson = new Gson();
                                currentUserProgram = gson.fromJson(jsonObject.toString(), UserProgram.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUserProgram, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;  //<==
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached User", this.currentUserProgram, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    public void postUserProgram(Context context, long app_program_type_id, long user_id, String name, String description, int use_timing) {
        appendCall("postUserProgram");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("app_program_type_id", String.valueOf(app_program_type_id));
        params.put("user_id", String.valueOf(user_id));
        params.put("name", name);
        params.put("description", description);
        params.put("use_timing", String.valueOf(use_timing));

        POST_GivenObject(context, "/user_programs/"/*+API_KEY_ENDPOINT*/, params, KEY_CLASS_UserProgram);
    }

    public void putUserProgram(Context context, String rid, long user_id, long app_program_type_id, String name, String description, int use_timing) {
        appendCall("putUserProgram");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("user_id", String.valueOf(user_id));
        params.put("app_program_type_id", String.valueOf(app_program_type_id));
        params.put("name", name);
        params.put("description", description);
        params.put("use_timing", String.valueOf(use_timing));

        PUT_GivenObject(context, "/user_programs/", params, KEY_CLASS_UserProgram);
    }

    public void deleteUserProgram(Context context, String rid) {
        appendCall("deleteUserProgram");
        DELETE_GivenObject(context, "/user_programs/", KEY_CLASS_UserProgram, rid + API_KEY_ENDPOINT);
    }
    //</editor-fold>

    //<editor-fold desc="/user_program_sessions/">
    public void getUserProgramSession(Context context, String rid, boolean forceDownload) {
        appendCall("getUserProgramSession");

        if (forceDownload || this.currentUserProgramSession == null) {
            // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "/user_program_sessions/" + rid + API_KEY_ENDPOINT;
                appendUrlString(mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;   //<==
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;  //<==
                                Gson gson = new Gson();
                                currentUserProgramSession = gson.fromJson(jsonObject.toString(), UserProgramSession.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUserProgramSession, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;  //<==
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached CurrentUserProgramSession", this.currentUserProgramSession, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    public void postUserProgramSession(Context context, long user_program_id, String date, int time_spent, String description, String extra_json_data) {
        appendCall("postUserProgramSession");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("user_program_id", String.valueOf(user_program_id));
        params.put("date", date);
        params.put("time_spent", String.valueOf(time_spent));
        params.put("description", description);
        params.put("extra_json_data", extra_json_data);

        POST_GivenObject(context, "/user_program_sessions/", params, KEY_CLASS_UserProgramSession);
    }

    public void putUserProgramSession(Context context, String rid, long user_program_id, String date, int time_spent, String description, String extra_json_data) {
        appendCall("putUserProgramSession");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("user_program_id", String.valueOf(user_program_id));
        params.put("date", date);
        params.put("time_spent", String.valueOf(time_spent));
        params.put("description", description);
        params.put("extra_json_data", extra_json_data);

        PUT_GivenObject(context, "/user_program_sessions/", params, KEY_CLASS_UserProgramSession);
    }

    public void deleteUserProgramSession(Context context, String rid) {
        appendCall("deleteUserProgramSession");
        DELETE_GivenObject(context, "/user_program_sessions/", KEY_CLASS_UserProgramSession, rid + API_KEY_ENDPOINT);
    }
    //</editor-fold>

    //<editor-fold desc="/user_program_exercises/">
    public void getUserProgramExercise(Context context, String rid, boolean forceDownload) {
        appendCall("getUserProgramExercise");

        if (forceDownload || this.currentUserProgramExercise == null) {
            // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
            if (!this.downloading) {
                String mUrlString = BASE_URL_ENDPOINTS + "/user_program_exercises/" + rid + API_KEY_ENDPOINT;
                appendUrlString(mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;   //<==
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;  //<==
                                Gson gson = new Gson();
                                currentUserProgramExercise = gson.fromJson(jsonObject.toString(), UserProgramExercise.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUserProgramExercise, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;  //<==
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached currentUserProgramExercise", this.currentUserProgramExercise, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    public void postUserProgramExercise(Context context, long user_program_id, long app_exercise_id) {
        appendCall("postUserProgramExercise");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("user_program_id", String.valueOf(user_program_id));
        params.put("app_exercise_id", String.valueOf(app_exercise_id));

        POST_GivenObject(context, "/user_program_exercises/", params, KEY_CLASS_UserProgramExercise);
    }

    public void putUserProgramExercise(Context context, String rid, long user_program_id, long app_exercise_id) {
        appendCall("putUserProgramExercise");
        final HashMap<String, String> params = new HashMap<>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("user_program_id", String.valueOf(user_program_id));
        params.put("app_exercise_id", String.valueOf(app_exercise_id));

        PUT_GivenObject(context, "/user_program_exercises/", params, KEY_CLASS_UserProgramExercise);
    }

    public void deleteUserProgramExercise(Context context, String rid) {
        appendCall("deleteUserProgramExercise");
        DELETE_GivenObject(context, "/user_program_exercises/", KEY_CLASS_UserProgramExercise, rid + API_KEY_ENDPOINT);
    }
    //</editor-fold>

    public void getIconFileNames(Context context, boolean forceDownload) {
        appendCall("getIconFileNames");

        if (forceDownload || this.currentIconFileNames == null) {
            if (!this.downloading) {
                String mUrlString = "https://tusk.systems/trainingapp/v2/iconfilenames.php";
                appendUrlString(mUrlString);
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                downloading = true;   //<==
                myJsonGetRequest = new MyJsonObjectRequest(
                        Request.Method.GET,
                        mUrlString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                downloading = false;  //<==
                                Gson gson = new Gson();
                                currentIconFileNames = gson.fromJson(jsonObject.toString(), IconFileNames.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentIconFileNames, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloading = false;  //<==
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) {
                };
                queue.add(myJsonGetRequest);
            }
        } else {
            ApiResponse resp = new ApiResponse(true, "OK, bruker cached iconFileNames", this.currentIconFileNames, myJsonGetRequest.getHttpStatusCode());
            apiResponse.postValue(resp);
        }
    }

    //<editor-fold desc="HTTP_QUERIES">

    /**
     * DELETE an object, use KEY_<CLASSNAME> String to specify it's Class
     * *Not to be used with /users/ for deletion*
     */
    private void DELETE_GivenObject(Context context, String api_subDirectory, String keyClassType, String restApiParams) {
        String mUrlString = BASE_URL_ENDPOINTS + api_subDirectory + "/" + restApiParams;
        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        Utils.appendLogger(mUrlString);
        myJsonDeleteRequest = new MyJsonObjectRequest(
                Request.Method.DELETE,
                mUrlString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResponse resp = new ApiResponse();
                        switch (keyClassType) {
                            case KEY_CLASS_AppProgramType:
                                resp = new ApiResponse(true, "OK", (AppProgramType) null, myJsonDeleteRequest.getHttpStatusCode());
                                break;
                            case KEY_CLASS_UserProgram:
                                resp = new ApiResponse(true, "OK", (UserProgram) null, myJsonDeleteRequest.getHttpStatusCode());
                                break;
                            case KEY_CLASS_UserProgramSession:
                                resp = new ApiResponse(true, "OK", (UserProgramSession) null, myJsonDeleteRequest.getHttpStatusCode());
                                break;
                            case KEY_CLASS_UserProgramExercise:
                                resp = new ApiResponse(true, "OK", (UserProgramExercise) null, myJsonDeleteRequest.getHttpStatusCode());
                                break;

                        }

                        apiResponse.postValue(resp);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiError apiError = VolleyErrorParser.parse(error);
                        errorMessage.postValue(apiError);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(myJsonDeleteRequest);
    }

    /**
     * PUT an object, use KEY_<CLASSNAME> String to specify it's Class
     */
    private void PUT_GivenObject(Context context, String api_subDirectory, final HashMap<String, String> params, String keyClassType) {
        String mUrlString = BASE_URL_ENDPOINTS + api_subDirectory;
        appendCall("PUT_GivenObject");
        Utils.appendLogger(params.toString());
        appendUrlString(mUrlString);

        queue = MySingletonQueue.getInstance(context).getRequestQueue();
        myJsonPutRequest = new MyJsonObjectRequest(
                Request.Method.PUT,
                mUrlString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject jsonObject = response.getJSONObject("record");
                            ApiResponse internalApiResponse = new ApiResponse();
                            switch (keyClassType) {
                                case KEY_CLASS_AppProgramType:
                                    internalApiResponse = new ApiResponse(true, message, (AppProgramType) gson.fromJson(jsonObject.toString(), AppProgramType.class), myJsonPutRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_UserProgram:
                                    internalApiResponse = new ApiResponse(true, message, (UserProgram) gson.fromJson(jsonObject.toString(), UserProgram.class), myJsonPutRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_UserProgramSession:
                                    internalApiResponse = new ApiResponse(true, message, (UserProgramSession) gson.fromJson(jsonObject.toString(), UserProgramSession.class), myJsonPutRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_UserProgramExercise:
                                    internalApiResponse = new ApiResponse(true, message, (UserProgramExercise) gson.fromJson(jsonObject.toString(), UserProgramExercise.class), myJsonPutRequest.getHttpStatusCode());
                                    break;
                            }
                            apiResponse.postValue(internalApiResponse);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiError apiError = VolleyErrorParser.parse(error);
                        errorMessage.postValue(apiError);
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(myJsonPutRequest);
    }

    /**
     * POST an object, use KEY_<CLASSNAME> String to specify it's Class
     */
    private void POST_GivenObject(Context context, String api_subDirectory, final HashMap<String, String> params, String keyClassType) {
        String mUrlString = BASE_URL_ENDPOINTS + api_subDirectory;
        appendUrlString(mUrlString);
        Utils.appendLogger("" + params.toString());
        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonPostRequest = new MyJsonObjectRequest(
                Request.Method.POST,
                mUrlString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject jsonObject = response.getJSONObject("record");
                            ApiResponse internalApiResponse = new ApiResponse();

                            switch (keyClassType) {
                                case KEY_CLASS_AppProgramType:
                                    internalApiResponse = new ApiResponse(true, message, (AppProgramType) gson.fromJson(jsonObject.toString(), AppProgramType.class), myJsonPostRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_AppExercise:
                                    internalApiResponse = new ApiResponse(true, message, (AppExercise) gson.fromJson(jsonObject.toString(), AppExercise.class), myJsonPostRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_UserProgram:
                                    internalApiResponse = new ApiResponse(true, message, (UserProgram) gson.fromJson(jsonObject.toString(), UserProgram.class), myJsonPostRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_UserProgramSession:
                                    internalApiResponse = new ApiResponse(true, message, (UserProgramSession) gson.fromJson(jsonObject.toString(), UserProgramSession.class), myJsonPostRequest.getHttpStatusCode());
                                    break;
                                case KEY_CLASS_UserProgramExercise:
                                    internalApiResponse = new ApiResponse(true, message, (UserProgramExercise) gson.fromJson(jsonObject.toString(), UserProgramExercise.class), myJsonPostRequest.getHttpStatusCode());
                                    break;
                            }
                            apiResponse.postValue(internalApiResponse);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiError apiError = VolleyErrorParser.parse(error);
                        errorMessage.postValue(apiError);
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(myJsonPostRequest);

    }
    //</editor-fold>

    private void appendCall(String methodName) {
        Utils.appendLogger("Call -> DataRepository." + methodName);
    }

    private void appendUrlString(String mUrlString) {
        Utils.appendLogger("mUrlString: " + mUrlString);
    }
}
