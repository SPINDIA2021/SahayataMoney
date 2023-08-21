package com.spindia.sahayatamoney.data.remote;




import com.myapp.onlysratchapp.category.CategoryResponse;
import com.spindia.sahayatamoney.data.DataSource;
import com.spindia.sahayatamoney.network.BaseResponse;
import com.spindia.sahayatamoney.network.IApi;
import com.spindia.sahayatamoney.network.NetworkCall;
import com.spindia.sahayatamoney.network.ServiceCallBack;


import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }




    @Override
    public void getCategory(ServiceCallBack myAppointmentPresenter, NetworkCall networkCall) {
        try{

            RequestBody for1 = RequestBody.create(MediaType.parse("text/plain"), "appcategory");
            Call<BaseResponse<ArrayList<CategoryResponse>>> responceCall = networkCall.getRetrofit(true, true).getCategory(for1);
            networkCall.setServiceCallBack(myAppointmentPresenter);
            networkCall.setRequestTag(IApi.COMMON_TAG);
            responceCall.enqueue(networkCall.requestCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


