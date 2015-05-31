package co.mitoo.sashimi.views.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.Contact;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;

/**
 * Created by david on 15-05-31.
 */
public class ContactAdapter extends ArrayAdapter<Contact> implements AdapterView.OnItemClickListener {

    private List<Contact> contact;

    public ContactAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.contact = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.view_network_list, null);
        }
        Contact contact = this.getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.profileImage);
        TextView nameView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView positionView = (TextView) convertView.findViewById(R.id.positionTextView);
        TextView firstTextView = (TextView) convertView.findViewById(R.id.firstTextView);
        TextView secondTextView = (TextView) convertView.findViewById(R.id.secondTextView);
        TextView thirdTextView = (TextView) convertView.findViewById(R.id.thirdTextView);

        //Set Views
        nameView.setText(contact.getName());
        positionView.setText(contact.getPosition());
        firstTextView.setText(contact.getFriend());
        secondTextView.setText(contact.getInterest());
        thirdTextView.setText(contact.getNeed());
        imageView.setImageResource(contact.getImageUrl());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                        .setFragmentID(R.id.fragment_contact)
                        .setTransition(MitooEnum.FragmentTransition.PUSH)
                        .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                        .build();
                BusProvider.post(fragmentChangeEvent);
            }
        });
        return convertView;

    }

    @Override

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




        }

}
