package com.jcmels.liba.piliplaydemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.jcmels.liba.piliplaydemo.adapter.QuickAdapter;
import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.bean.LiveStreamItem;
import com.jcmels.liba.piliplaydemo.bean.LiveStreamsDataBean;
import com.jcmels.liba.piliplaydemo.data.DataServer;
import com.jcmels.liba.piliplaydemo.bean.msgEvent;
import com.jcmels.liba.piliplaydemo.activity.play.PLVideoViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<LiveStreamItem> liveStreamItems;
    private OnFragmentInteractionListener mListener;
    private android.support.v7.widget.RecyclerView rvhottab;
    private QuickAdapter adapter;
    private DataServer dataServer;
    private Boolean IsFirstIn = true;
    private Boolean ISFirstLoad = true;
    private android.support.v4.widget.SwipeRefreshLayout swiperefresh;

    public HotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotFragment newInstance(String param1, String param2) {
        HotFragment fragment = new HotFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot, container, false);

        EventBus.getDefault().register(this);
        System.out.println("注册EventBus");
        initView(view);

        initData();
        dataServer = new DataServer();
        dataServer.start_retrofit();
        dataServer.getLiveStreams();

        return view;
    }

    private void initView(View view) {
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(this);
        swiperefresh.setColorSchemeResources(R.color.srl_color1, R.color.srl_color2, R.color.srl_color3, R.color.srl_color4);
        rvhottab = (RecyclerView) view.findViewById(R.id.rv_hottab);
        rvhottab.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new QuickAdapter(null);
        rvhottab.setAdapter(adapter);
        rvhottab.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
                intent.putExtra("playurl", liveStreamItems.get(i).getRtmpurl());
                intent.putExtra("streamkey", liveStreamItems.get(i).getKey());
                intent.putExtra("roomname", liveStreamItems.get(i).getPiliroomname());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        liveStreamItems = new ArrayList<>();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listEvent(LiveStreamsDataBean liveStreamDataBean) {
        liveStreamItems.clear();
        liveStreamItems.addAll(liveStreamDataBean.getList());
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
        adapter.setNewData(liveStreamItems);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listEvent(msgEvent msgEvent) {
        Toast.makeText(getActivity(), msgEvent.getMsg(), Toast.LENGTH_SHORT).show();
        adapter.setNewData(null);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        System.out.println("销毁EventBus");
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataServer.getLiveStreams();
                swiperefresh.setRefreshing(false);
            }
        }, 1000);

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
}
