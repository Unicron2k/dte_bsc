package com.example.thelastdeath.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.repository.DataRepository;

/**
 @author 
 */
public class DataViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private MutableLiveData<ApiError> mApiError;
    private MutableLiveData<ApiResponse> mApiResponse;

    public DataViewModel(Application application) {
        super(application); // <====
        mRepository = new DataRepository(application);
        mApiError = mRepository.getErrorMessage();
        mApiResponse = mRepository.getApiResponse();
    }

    public MutableLiveData<ApiError> getApiError() {
        return mApiError;
    }

    public MutableLiveData<ApiResponse> getApiResponse() {
        return mApiResponse;
    }

    //<editor-fold desc="user">
    // GET user
    public void getUser(Context context, String firebase_id, boolean force_download) {
        mRepository.getUser(context, firebase_id, force_download);
    }

    public void getUserExpandedchildren(Context context, String firebase_id, boolean force_download) {
        mRepository.getUserExpandedchildren(context, firebase_id, force_download);
    }

    // POST user
    public void postUser(Context context, String firebase_id, String name, String phone, String email, int birth_year) {
        mRepository.postUser(context, firebase_id, name, phone, email, birth_year);
    }

    // PUT user
    public void putUser(Context context, String firebase_id, String name, String phone, String email, int birth_year) {
        mRepository.putUser(context, firebase_id, name, phone, email, birth_year);
    }

    // DELETE user
    public void deleteUser(Context context, String firebase_id) {
        mRepository.deleteUser(context, firebase_id);
    }
    //</editor-fold>

    //<editor-fold desc="user_stats">
    // GET user_stats
    public void getUserStats(Context context, String firebase_id, boolean force_download) {
        mRepository.getUserStats(context, firebase_id, force_download);
    }
    //</editor-fold>

    //<editor-fold desc="/app_program_types/">
    public void getAllAppProgramTypes(Context context, boolean force_download) {
        mRepository.getAllAppProgramTypes(context, force_download);
    }

    public void getAppProgramTypeFromRid(Context context, String rid, boolean force_download) {
        mRepository.getAppProgramTypeFromRid(context, rid, force_download);
    }

    public void postAppProgramType(Context context, String description, String icon, String back_color) {
        mRepository.postAppProgramType(context, description, icon, back_color);
    }

    public void putAppProgramType(Context context, String rid, String description, String icon, String back_color) {
        mRepository.putAppProgramType(context, rid, description, icon, back_color);
    }

    public void deleteAppProgramType(Context context, String rid) {
        mRepository.deleteAppProgramType(context, rid);
    }
    //</editor-fold>get

    //<editor-fold desc="/app_exercises/">
    public void getAllAppExercises(Context context, boolean force_download) {
        mRepository.getAllAppExercises(context, force_download);
    }

    public void getAppExercise(Context context, String rid, boolean force_Download) {
        mRepository.getAppExerciseFromRid(context, rid, force_Download);
    }

    public void postAppExercise(Context context, String name, String description, String icon, String infobox_color) {
        mRepository.postAppExercise(context, name, description, icon, infobox_color);
    }

    public void putAppExercise(Context context, String rid, String name, String description, String icon, String infobox_color) {
        mRepository.putAppExercise(context, rid, name, description, icon, infobox_color);
    }

    public void deleteAppExercise(Context context, String rid) {
        mRepository.deleteAppExercise(context, rid);
    }
    //</editor-fold>

    //<editor-fold desc="/user_programs/">
    public void getUserProgram(Context context, String rid, boolean forceDownload) {
        mRepository.getUserProgram(context, rid, forceDownload);
    }

    public void postUserProgram(Context context, long app_program_type_id, long user_id, String name, String description, int use_timing) {
        mRepository.postUserProgram(context, app_program_type_id, user_id, name, description, use_timing);
    }

    public void putUserProgram(Context context, String rid, long user_id, long app_program_type_id, String name, String description, int use_timing) {
        mRepository.putUserProgram(context, rid, user_id, app_program_type_id, name, description, use_timing);
    }

    public void deleteUserProgram(Context context, String rid) {
        mRepository.deleteUserProgram(context, rid);
    }
    //</editor-fold>

    //<editor-fold desc="/user_program_sessions/">
    public void getUserProgramSession(Context context, String rid, boolean forceDownload) {
        mRepository.getUserProgramSession(context, rid, forceDownload);
    }

    public void postUserProgramSession(Context context, long user_program_id, String date, int time_spent, String description, String extra_json_data) {
        mRepository.postUserProgramSession(context, user_program_id, date, time_spent, description, extra_json_data);
    }

    public void putUserProgramSession(Context context, String rid, long user_program_id, String date, int time_spent, String description, String extra_json_data) {
        mRepository.putUserProgramSession(context, rid, user_program_id, date, time_spent, description, extra_json_data);
    }

    public void deleteUserProgramSession(Context context, String rid) {
        mRepository.deleteUserProgramSession(context, rid);
    }
    //</editor-fold>

    //<editor-fold desc="/user_program_exercises/">
    public void getUserProgramExercise(Context context, String rid, boolean forceDownload) {
        mRepository.getUserProgramExercise(context, rid, forceDownload);
    }

    public void postUserProgramExercise(Context context, long user_program_id, long app_exercise_id) {
        mRepository.postUserProgramExercise(context, user_program_id, app_exercise_id);
    }

    public void putUserProgramExerciseToServer(Context context, String rid, long user_program_id, long app_exercise_id) {
        mRepository.putUserProgramExercise(context, rid, user_program_id, app_exercise_id);
    }

    public void deleteUserProgramExercise(Context context, String rid) {
        mRepository.deleteUserProgramExercise(context, rid);
    }
    //</editor-fold>

    public void getIconFileNames(Context context, boolean forceDownload) {
        mRepository.getIconFileNames(context, forceDownload);
    }
}
