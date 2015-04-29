package co.mitoo.sashimi.utils;

import android.widget.ImageView;
import android.widget.TextView;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Team;

/**
 * Created by david on 15-04-28.
 */
public class TeamViewModel {

    private ViewHelper viewHelper;

    public TeamViewModel(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    public void loadTeamIcon(ImageView imageView, Team team){

        String iconUrl = getTeamLogo(team);
        if(iconUrl!= null && imageView !=null){
            this.viewHelper.getPicasso().with(this.viewHelper.getActivity())
                    .load(iconUrl)
                    .error(R.drawable.team_logo_tbc)
                    .into(imageView);
        }
    }

    public String getTeamLogo(Team team) {

        String result = null;
        if (team != null) {
            result = this.viewHelper.getRetinaUrl(team.getLogo_small());
        }
        return result;
    }

    public void setUpTeamName(Team team ,TextView textView) {

        if (team != null)
            textView.setText(team.getName());
        else
            setTextViewAsTBC(textView);

    }

    public void setTextViewAsTBC(TextView textView){

        textView.setText(this.viewHelper.getActivity().getString(R.string.fixture_page_tbd));
        textView.setTextAppearance(this.viewHelper.getActivity(), R.style.schedulePageTBCText);

    }
}
