package xyz.grand.grandeur;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ferick Andrew on Mar 21, 2017.
 */

public class FragmentChat extends Fragment
{
    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout rl_chat;
    ListView msgList;
    FloatingActionButton fab;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sign_out)
        {
            AuthUI.getInstance().signOut(this.getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    Snackbar.make(rl_chat, "You have been signed out.", Snackbar.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
            return true;
        }
        else { return false; }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        inflater.inflate(R.menu.menu_main, menu);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Snackbar.make(rl_chat, "Successfully signed in! \n Welcome, ", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                Snackbar.make(rl_chat, "Unable to sign you in, \n Please try again later.", Snackbar.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View fragView = inflater.inflate(R.layout.fragment_chat, container, false);

        msgList = (ListView) fragView.findViewById(R.id.message_list);
        rl_chat = (RelativeLayout) fragView.findViewById(R.id.fragment_chat);
        fab = (FloatingActionButton) fragView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input_message = (EditText) v.findViewById(R.id.input_message);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input_message.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input_message.setText("");
            }
        });

        // Check if not signed in
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
//            Intent authUI = new Intent(this.getActivity(), LoginActivity.class);
//            startActivityForResult(authUI, SIGN_IN_REQUEST_CODE);
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(rl_chat, "Welcome, " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            //Load content
            displayChatMessage();
        }
        return fragView;
    }

    private void displayChatMessage()
    {
        //msgList = (ListView) getView().findViewById(R.id.message_list);  //This returns null reference
        adapter = new FirebaseListAdapter<ChatMessage>(this.getActivity(), ChatMessage.class, R.layout.list_chat_item, FirebaseDatabase.getInstance().getReference())
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        msgList.setAdapter(adapter);
    }
}