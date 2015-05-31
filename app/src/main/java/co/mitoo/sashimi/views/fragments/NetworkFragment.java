package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.Contact;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.views.adapter.ContactAdapter;

/**
 * Created by david on 15-05-30.
 */

public class NetworkFragment extends  MitooFragment {

    private ListView networklistView;
    private List<Contact> contactList = new ArrayList<Contact>();
    private ContactAdapter contactAdapter ;

    public static NetworkFragment newInstance() {
        NetworkFragment fragment = new NetworkFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_network,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentChangeEvent fragmentChangeEvent ;
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.networkListView:
                    fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                            .setFragmentID(R.id.fragment_location_selection)
                            .setTransition(MitooEnum.FragmentTransition.PUSH)
                            .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                            .build();
                    BusProvider.post(fragmentChangeEvent);
                    break;
            }
        }

    }

    @Override
    protected void initializeOnClickListeners(View view){


    }

    @Override
    protected void initializeViews(View view){
        initializeOnClickListeners(view);
        initializeArrayList();
        this.networklistView = (ListView) view.findViewById(R.id.networkListView);
        this.contactAdapter = new ContactAdapter(getActivity(), R.id.networkListView,this.contactList);
        this.networklistView.setAdapter(this.contactAdapter);
        this.contactAdapter.notifyDataSetChanged();
    }

    private void initializeArrayList(){

            Contact contact = new Contact();
        contact.setName("MR ABC");
        contact.setPosition("CEO OF Uber");
        contact.setFriend("50");
        contact.setInterest("25");
        contact.setNeed("200");
        contact.setImageUrl(R.drawable.untitled2_09);
        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("MS Smith");
        contact.setPosition("CTO OF Too Cool");
        contact.setFriend("50");
        contact.setInterest("35");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_08);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("MR Beckham");
        contact.setPosition("CFO OF For School");
        contact.setFriend("50");
        contact.setInterest("25");
        contact.setNeed("10");
        contact.setImageUrl(R.drawable.untitled2_07);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("Sam Smith");
        contact.setPosition("CMO OF Sick App");
        contact.setFriend("50");
        contact.setInterest("35");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_06);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("John Doe");
        contact.setPosition("COO OF Google");
        contact.setFriend("510");
        contact.setInterest("25");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_05);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("John Doe");
        contact.setPosition("COO OF Google");
        contact.setFriend("510");
        contact.setInterest("25");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_04);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("John Doe");
        contact.setPosition("COO OF Google");
        contact.setFriend("510");
        contact.setInterest("25");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_03);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("John Doe");
        contact.setPosition("COO OF Google");
        contact.setFriend("510");
        contact.setInterest("25");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_02);

        this.contactList.add(contact);

        contact = new Contact();
        contact.setName("John Doe");
        contact.setPosition("COO OF Google");
        contact.setFriend("510");
        contact.setInterest("25");
        contact.setNeed("80");
        contact.setImageUrl(R.drawable.untitled2_01);

        this.contactList.add(contact);
    }



}
