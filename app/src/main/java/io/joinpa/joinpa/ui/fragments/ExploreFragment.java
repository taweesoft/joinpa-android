package io.joinpa.joinpa.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.joinpa.joinpa.R;
import io.joinpa.joinpa.managers.App;
import io.joinpa.joinpa.managers.Commands.GetPublicEventResponse;
import io.joinpa.joinpa.managers.Commands.ObjectResponse;
import io.joinpa.joinpa.models.Event;
import io.joinpa.joinpa.models.EventElement;
import io.joinpa.joinpa.models.Message;
import io.joinpa.joinpa.models.Place;
import io.joinpa.joinpa.ui.adapters.EventAdapter;
import io.joinpa.joinpa.util.ProgressDialogUtil;
import retrofit2.Response;

/**
 * Created by TAWEESOFT on 5/15/16 AD.
 */
public class ExploreFragment extends ObservableFragment implements Observer {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.event_recyclerView)
    RecyclerView recyclerView;

    private EventAdapter adapter;
    private List<Event> events;

    @Override
    public void update(java.util.Observable observable, Object data) {

        if (data == null) return;
        if (!(data instanceof ObjectResponse)) return;

        ObjectResponse objectResponse = (ObjectResponse) data;

        if (objectResponse.isSuccess()) {
            if (((Response)objectResponse.getData()).body().getClass() == EventElement.class) {
                Response<EventElement> response = (Response<EventElement>) objectResponse.getData();
                events.clear();
                events.addAll(response.body().getEventList());
                adapter.notifyDataSetChanged();
            } else {
                Response<Message> response = (Response<Message>) objectResponse.getData();
                ProgressDialogUtil.hide();
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Log.e("error", objectResponse.getMessage());
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this,view);

        initComponents();

        GetPublicEventResponse response = new GetPublicEventResponse();
        response.addObserver(this);
        response.execute();


        return view;
    }

    private void initComponents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChanged();
                notifyObservers();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        events = new ArrayList<Event>();

        adapter = new EventAdapter(this.getContext(), events);
        adapter.setObserver(this);
        recyclerView.setAdapter(adapter);
    }

}
