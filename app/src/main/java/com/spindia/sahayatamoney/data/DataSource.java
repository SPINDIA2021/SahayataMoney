package com.spindia.sahayatamoney.data;




import com.spindia.sahayatamoney.network.NetworkCall;
import com.spindia.sahayatamoney.network.ServiceCallBack;


public interface DataSource {

    void getCategory(ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

}

