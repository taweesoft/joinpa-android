package io.joinpa.joinpa.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.joinpa.joinpa.R;
import io.joinpa.joinpa.managers.App;
import io.joinpa.joinpa.managers.commands.JoinEventResponse;
import io.joinpa.joinpa.models.Event;
import io.joinpa.joinpa.ui.views.EventActivity;
import io.joinpa.joinpa.util.DateUtil;
import io.joinpa.joinpa.util.ProgressDialogUtil;

/**
 * Created by Peter on 5/20/2016 AD.
 */
public class ExploreEventAdapter extends RecyclerView.Adapter<ExploreEventAdapter.ViewHolder> {

    private App app;

    private Context context;
    private List<Event> events;
    private Observer observer;

    public ExploreEventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        app = App.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_explore, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Event event = events.get(position);
        Date date = event.getDate();
        int avatar = event.getHost().getAvatar();
        int eventImg = event.getIcon();
        int numJoined = event.getJoinedList().size();
        String joinText = "Be the first to join this event";
        if (numJoined > 0) joinText = numJoined + " people joined";

        holder.userImage.setImageResource(app.getInternalData().avatarNormal[avatar]);
        holder.eventImage.setImageResource(app.getInternalData().eventIcon[eventImg]);

        holder.username.setText(event.getHost().getUsername());
        holder.eventName.setText(event.getName());
        holder.numJoined.setText(joinText);
        holder.dateLabel.setText(DateUtil.getDate(date));
        holder.timeLabel.setText(DateUtil.getTime(date));
        holder.location.setText(event.getPlace().getName());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_user)
        ImageView userImage;

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.image_event_icon)
        ImageView eventImage;

        @BindView(R.id.text_event_name)
        TextView eventName;

        @BindView(R.id.text_num_joined)
        TextView numJoined;

        @BindView(R.id.text_date)
        TextView dateLabel;

        @BindView(R.id.text_time)
        TextView timeLabel;

        @BindView(R.id.text_location)
        TextView location;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @OnClick(R.id.btn_join)
        public void joinEvent() {
            Event event = events.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());

            ProgressDialogUtil.show(context, "Joining event..");
            JoinEventResponse response = new JoinEventResponse(event.getId());
            response.addObserver(observer);
            response.execute();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("event_index" , getAdapterPosition());
            context.startActivity(intent);
        }
    }

}
