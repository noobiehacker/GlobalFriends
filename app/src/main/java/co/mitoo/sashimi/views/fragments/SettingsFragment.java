package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.views.Dialog.AboutMitooDialogBuilder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by david on 15-01-12.
 */
public class SettingsFragment extends MitooFragment {

    private UserInfoRecieve userInfoRecieve;
    
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_settings,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.about_text_view:
                aboutMitooAction();
                break;
            case R.id.log_out_text_view:
                logtOutAction();
                break;
            case R.id.feedback_text_view:
                feedBackAction();
                break;
        }
    }
                
    @Override
    protected void initializeFields(){

        setFragmentTitle(getString(R.string.tool_bar_settings));
        setUserInfoRecieve(getUserInfoModel().getUserInfoRecieve());

    }
    
    @Override
    protected void initializeViews(View view){

        setUpToolBar(view);
        setUpUserDetails(view);

    }

    private void initializeOnClickListeners(View view){

        view.findViewById(R.id.about_text_view).setOnClickListener(this);
        view.findViewById(R.id.log_out_text_view).setOnClickListener(this);
        view.findViewById(R.id.feedback_text_view).setOnClickListener(this);

    }

    public UserInfoRecieve getUserInfoRecieve() {
        return userInfoRecieve;
    }

    public void setUserInfoRecieve(UserInfoRecieve userInfoRecieve) {
        this.userInfoRecieve = userInfoRecieve;
    }
    
    private void setUpUserDetails(View view){
        
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.user_profileImage);
        Picasso.with(getActivity())
                .load(getUserInfoRecieve().picture_thumb)
                .into(imageView);
        
        TextView nameTextView= (TextView) view.findViewById(R.id.user_name);
        nameTextView.setText(getUserInfoRecieve().name);
    }
    
    private void logtOutAction(){
        
        getMitooActivity().logOut();
        
    }

    private void aboutMitooAction(){

        AboutMitooDialogBuilder dialog = new AboutMitooDialogBuilder(getActivity());
        dialog.buildPrompt().show();

    }
    
    private void feedBackAction(){

        getMitooActivity().contactMitoo();

    }
}
