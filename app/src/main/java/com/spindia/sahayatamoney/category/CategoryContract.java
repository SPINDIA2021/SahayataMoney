package com.spindia.sahayatamoney.category;


import android.content.Context;

import com.myapp.onlysratchapp.category.CategoryResponse;
import com.spindia.sahayatamoney.network.BaseView;

import java.util.ArrayList;


public interface CategoryContract {
    interface View extends BaseView<Presenter> {

        void categoryResponse(ArrayList<CategoryResponse> categoryResponse);

    }

    interface Presenter {


        void getCategory( Context context);


    }
}
