package nanodegree.thiago.bakingapp_stage1;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, String ingredients) {
        Log.d("Teste", "Atualizando widget");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_widget);
        views.setTextViewText(R.id.widget_ingredients_text, ingredients);
        views.setTextViewText(R.id.widget_recipe_name_textview, recipeName);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidgets(Context context, String recipeName,
                                     List<RecipeJson.IngredientsBean> ingredients) {
        StringBuilder formattedIngredients = new StringBuilder();
        for (RecipeJson.IngredientsBean ingredient : ingredients) {
            formattedIngredients.append("* " + ingredient.getQuantity() + " "
                        + ingredient.getMeasure()
                        + " - " + ingredient.getIngredient() + "\n");
        }
        ComponentName componentName = new ComponentName(context, RecipesWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int []appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context,
                    appWidgetManager,
                    appWidgetId,
                    recipeName,
                    formattedIngredients.toString());
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Widget will be only updated by the APP, so, it doesn't need to take action here
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

