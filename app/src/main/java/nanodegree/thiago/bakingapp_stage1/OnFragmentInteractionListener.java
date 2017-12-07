package nanodegree.thiago.bakingapp_stage1;

import android.os.Bundle;

/**
 * Created by thiagom on 12/5/17.
 */

public interface OnFragmentInteractionListener {

    public static final int ACTION_INVALID = -1;
    public static final int ACTION_RECIPE_SELECTED = 0;
    public static final int ACTION_STEP_SELECTED = 1;
    public static final int ACTION_INGREDIENTS_SELECTED = 2;
    public static final int ACTION_NEXT_STEP = 3;
    public static final int ACTION_PREVIOUS_STEP = 4;

    public void onFragmentInteraction(int action, Bundle extras);
}
