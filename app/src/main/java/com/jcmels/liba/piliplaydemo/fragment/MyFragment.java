package com.jcmels.liba.piliplaydemo.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcmels.liba.piliplaydemo.activity.LoginActivity;
import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.bean.BaseRequestBean;
import com.jcmels.liba.piliplaydemo.bean.UserInfoBean;
import com.jcmels.liba.piliplaydemo.network.Stream;
import com.jcmels.liba.piliplaydemo.network.UriServer;
import com.jcmels.liba.piliplaydemo.activity.pili.HWCameraStreamingActivity;
import com.lb.materialdesigndialog.base.DialogBase;
import com.lb.materialdesigndialog.base.DialogWithTitle;
import com.lb.materialdesigndialog.impl.MaterialDialogInput;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean mPermissionEnabled = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private android.support.design.widget.FloatingActionButton fabstartpili;
    private com.facebook.drawee.view.SimpleDraweeView myfheadimg;
    private android.widget.RelativeLayout myflogin1;
    private com.facebook.drawee.view.SimpleDraweeView myfsubscribe;
    private com.facebook.drawee.view.SimpleDraweeView myfhistory;
    private com.facebook.drawee.view.SimpleDraweeView myfmsg;
    private com.facebook.drawee.view.SimpleDraweeView myfnotice;
private String piliurl;
    private android.widget.TextView myfusername;
    private Boolean IsLogin=false;
    private Retrofit retrofit;
    private Stream stream;
    private String streamKey;
    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my, container, false);



        initRetrofit();
        this.myfusername = (TextView) view.findViewById(R.id.myf_username);
        EventBus.getDefault().register(this);
        this.myfnotice = (SimpleDraweeView) view.findViewById(R.id.myf_notice);
        this.myfmsg = (SimpleDraweeView) view.findViewById(R.id.myf_msg);
        this.myfhistory = (SimpleDraweeView) view.findViewById(R.id.myf_history);
        this.myfsubscribe = (SimpleDraweeView) view.findViewById(R.id.myf_subscribe);
        this.myflogin1 = (RelativeLayout) view.findViewById(R.id.myf_login1);
        this.myfheadimg = (SimpleDraweeView) view.findViewById(R.id.myf_headimg);
        this.fabstartpili = (FloatingActionButton) view.findViewById(R.id.fab_startpili);
        myflogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
        fabstartpili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPermissionOK()) {
                    return;
                }
                if (IsLogin) {
                    showInputDialog();
//                    Intent intent = new Intent(getActivity(), HWCameraStreamingActivity.class);
//                    intent.putExtra("piliurl", piliurl);
//                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "请登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoEvent(UserInfoBean userInfo) {
        Toast.makeText(getActivity(), "欢迎回来"+userInfo.getUsername(), Toast.LENGTH_SHORT).show();
        piliurl=userInfo.getPiliurl();
        streamKey=userInfo.getStreamkey();
        IsLogin=true;
        myfusername.setText(userInfo.getUsername());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private boolean isPermissionOK() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionEnabled = true;
            return true;
        }
        else {
            return checkPermission();
        }
    }
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        boolean ret = true;

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("CAMERA");
        }
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) {
            permissionsNeeded.add("MICROPHONE");
        }
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write external storage");
        }

        if (permissionsNeeded.size() > 0) {
            // Need Rationale
            String message = "You need to grant access to " + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++) {
                message = message + ", " + permissionsNeeded.get(i);
            }
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permissionsList.get(0))) {
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
            }
            else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            ret = false;
        }

        return ret;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean ret = true;
        if (getContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            ret = false;
        }
        return ret;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 显示一个带输入框的Dialog
     */
    private void showInputDialog() {
        final MaterialDialogInput dialog = new MaterialDialogInput(getActivity());


        dialog.setTitle("创建直播间");

        dialog.setDesc("请输入房间标题：");


        dialog.setNegativeButton("取消", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                Toast.makeText(getActivity(), "你取消了直播", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("确定", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase d, View view) {
                String userInput = dialog.getUserInput();
                updateRoomName(userInput,streamKey);
                Intent intent = new Intent(getActivity(), HWCameraStreamingActivity.class);
                    intent.putExtra("piliurl", piliurl);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }


    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(UriServer.getBASEURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stream = retrofit.create(Stream.class);
    }

    private void updateRoomName(String roomname, String streamkey) {
        Call<BaseRequestBean> call = stream.updateRoomName(streamkey, roomname);
        call.enqueue(new Callback<BaseRequestBean>() {
            @Override
            public void onResponse(Call<BaseRequestBean> call, Response<BaseRequestBean> response) {
                if (response.body().getResultcode().equals("200")) {
                    Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResultcode().equals("404")) {

                } else if (response.body().getResultcode().equals("500")) {

                }
            }

            @Override
            public void onFailure(Call<BaseRequestBean> call, Throwable t) {

            }
        });
    }
}
