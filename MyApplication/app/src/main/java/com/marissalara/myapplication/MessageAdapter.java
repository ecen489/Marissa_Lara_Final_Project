package com.marissalara.myapplication;

import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

public class MessageAdapter extends FirebaseListAdapter<ChatMessage> {

    private MainActivity activity;

    //Firebase is nice in that its UI has a way that handles all of the child events at the given
    // Firebase location.
    //Because of this, the messages,dates, ect. are populated & handled through this function using a
    //custom Listview
    public MessageAdapter(MainActivity activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
    }

    @Override
    //Updates views
    protected void populateView(View v, ChatMessage model, int position) {
        //Text view for User Name, Time, and Actual Message
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);

        //Sets the user and message
        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());

        // Formats date before showing it
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
    }

    @Override
    //Creates 2 layouts corresponding to 2 types of messages (in and out).
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = getItem(position);
        if (chatMessage.getMessageUserId().equals(activity.getLoggedInUserName()))
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

        //Generates the View
        populateView(view, chatMessage, position);

        return view;
    }

    //This is just an override so there's only 2 ways to chat:
    // either you send a message or the other person does
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
