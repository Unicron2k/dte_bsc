package com.example.thelastdeath.utils.api;

import android.app.Activity;
import android.content.res.Resources;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.Utils;
import com.google.android.gms.common.util.ArrayUtils;

/**
 * 
 */
public final class UtilsAPI {
    public static final String API_KEY = /*"82879144-8856-11ea-b";*/"828f4143-8856-11ea-b";
    public static final String API_BASE_URL = "https://tusk.systems/trainingapp/v2/api.php/";
    private static final String MESSAGE_RECORD_DELETED = "Record deleted.";
    private static final String MESSAGE_RECORD_CREATED = "New record created. ";
    private static final String MESSAGE_RECORD_UPDATED = "Ok. Record updated.";


    /**
     * Displays custom message for ApiResponse as Toast
     */
    public static void displayApiResponseMessage(ApiResponse apiResponse, Activity activity) {
        internalDisplayResponsePretty(apiResponse.getHttpStatusCode(), apiResponse.getMessage(), activity, activity.getResources());
    }

    /**
     * Displays custom message for ApiError as Toast
     */
    public static void displayApiErrorResponseMessage(ApiError apiError, Activity activity) {
        internalDisplayResponsePretty(apiError.getCode(), apiError.getMessage(), activity, activity.getResources());
    }

    /**
     * Displays custom message according to given HTTP Status Code as Toast
     * @param responseCode HTTP Status Cose
     * @param apiMessage Message from REST API
     * @param activity Given Activity
     * @param resources Activity.getResources
     */
    private static void internalDisplayResponsePretty(int responseCode, String apiMessage, Activity activity, Resources resources) {
        // Consider adding more HTTP Response codes from the HTTP Protocol
        final int[] errorGET = {404, 500};
        final int[] errorPUT = {400};
        final int[] errorCommon = {409, 501, 502};
        String message = "";
        Utils.appendLogger(String.format("HTTP_RETURN_CODE [%s]= %s", responseCode, apiMessage));

        if (apiMessage.equals("Ok. Record updated."))
            message = resources.getString(R.string.responseCode_record_updated);
        if (apiMessage.equals("The request could not be completed due to a conflict with the current state of the resource:  Possible cause: duplicate key value."))
            return;
        else if (apiMessage.equals(MESSAGE_RECORD_CREATED))
            message = resources.getString(R.string.responseCode_record_created);
        else if (apiMessage.equals(MESSAGE_RECORD_DELETED))
            message = resources.getString(R.string.responseCode_record_deleted);
        else if (apiMessage.equals(MESSAGE_RECORD_UPDATED))
            message = resources.getString(R.string.responseCode_record_updated);
        // GET
        else if (responseCode == 200)
            return;//message = resources.getString(R.string.responseCode_200_GET_success);
        // POST
        else if (responseCode == 201)
            message = resources.getString(R.string.responseCode_201_POST_success);
        // PUT
        else if (responseCode == 202)
            message = resources.getString(R.string.responseCode_202_PUT_success);
        else if (ArrayUtils.contains(errorPUT, responseCode))
            message = resources.getString(R.string.responseCode_202_PUT_failure);
        // DELETE
        else if (responseCode == 203)
            message = resources.getString(R.string.responseCode_203_DELETE_success);

        // Display message
        if (!message.equals(""))
            Utils.displayToast(activity, message/* + "(" + apiMessage + ")"*/);
    }
}
