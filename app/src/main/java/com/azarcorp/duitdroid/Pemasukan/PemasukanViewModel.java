package com.azarcorp.duitdroid.Pemasukan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.azarcorp.duitdroid.database.DataBaseClient;
import com.azarcorp.duitdroid.database.DataBaseDao;
import com.azarcorp.duitdroid.model.ModelDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PemasukanViewModel extends AndroidViewModel {

    private LiveData<List<ModelDatabase>> mPemasukan;
    private DataBaseDao databaseDao;
    private LiveData<Integer> mTotalPrice;

    public PemasukanViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DataBaseClient.getInstance(application).getAppDatabase().databaseDao();
        mPemasukan = databaseDao.getAllPemasukan();
        mTotalPrice = databaseDao.getTotalPemasukan();
    }

    public LiveData<List<ModelDatabase>> getPemasukan() {
        return mPemasukan;
    }

    public LiveData<Integer> getTotalPemasukan() {
        return mTotalPrice;
    }

    public void deleteAllData() {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        databaseDao.deleteAllPemasukan();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public String deleteSingleData(final int uid) {
        String sKeterangan;
        try {
            Completable.fromAction(() -> databaseDao.deleteSinglePemasukan(uid))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            sKeterangan = "OK";
        } catch (Exception e) {
            sKeterangan = "NO";
        }
        return sKeterangan;
    }

}