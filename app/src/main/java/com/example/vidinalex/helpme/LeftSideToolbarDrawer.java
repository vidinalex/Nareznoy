package com.example.vidinalex.helpme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class LeftSideToolbarDrawer extends Drawer {

    private FirebaseAuth mAuth;

    LeftSideToolbarDrawer(final AppCompatActivity activity, android.support.v7.widget.Toolbar toolbar, final Drawer.Result result)
    {
        mAuth = FirebaseAuth.getInstance();
        withActivity(activity);
        withToolbar(toolbar);
        withActionBarDrawerToggle(true);
        withHeader(R.layout.drawer_header);
        addDrawerItems(
                new PrimaryDrawerItem().withName(R.string.drawer_item_profile).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                new PrimaryDrawerItem().withName(R.string.drawer_main_menu).withIcon(FontAwesome.Icon.faw_book),
                //new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                new SectionDrawerItem().withName(R.string.drawer_item_settings),
                new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                //new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                new DividerDrawerItem(),
                new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
            );
        withOnDrawerListener(new Drawer.OnDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                // Скрываем клавиатуру при открытии Navigation Drawer
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                }
            });
        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                // Обработка клика
                public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                    if (drawerItem instanceof Nameable) {

                        Toast.makeText(activity, activity.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();

                        if(activity.getString(((Nameable) drawerItem).getNameRes()) == activity.getString(R.string.drawer_item_profile) && mAuth.getCurrentUser() != null){
                            if(!(activity instanceof MainActivity))
                            {
                                Intent intent = new Intent(activity, ProfileActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            else
                            {
                                Intent intent = new Intent(activity, ProfileActivity.class);
                                activity.startActivity(intent);
                            }
                        }

                        if(activity.getString(((Nameable) drawerItem).getNameRes()) == activity.getString(R.string.drawer_item_profile) && mAuth.getCurrentUser() == null){
                            if(!(activity instanceof MainActivity))
                            {
                                Intent intent = new Intent(activity, LoginActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            else
                            {
                                Intent intent = new Intent(activity, LoginActivity.class);
                                activity.startActivity(intent);
                            }
                        }

                        if(activity.getString(((Nameable) drawerItem).getNameRes()) == activity.getString(R.string.drawer_main_menu)){
                            if(!(activity instanceof MainActivity))
                            {
                                activity.finish();
                            }
                        }

                        if(activity.getString(((Nameable) drawerItem).getNameRes()) == activity.getString(R.string.drawer_item_help)){
                            if(!(activity instanceof MainActivity))
                            {
                                Intent intent = new Intent(activity, SettingsActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            else
                            {
                                Intent intent = new Intent(activity, SettingsActivity.class);
                                activity.startActivity(intent);
                            }
                        }

                    }

                    if (drawerItem instanceof Badgeable) {

                        Badgeable badgeable = (Badgeable) drawerItem;

                        if (badgeable.getBadge() != null) {
                            // учтите, не делайте так, если ваш бейдж содержит символ "+"
                            try {

                                int badge = Integer.valueOf(badgeable.getBadge());

                                if (badge > 0) {
                                    result.updateBadge(String.valueOf(badge - 1), position);
                                }

                            } catch (Exception e) {
                                Log.d("test", "Не нажимайте на бейдж, содержащий плюс! :)");
                            }
                        }
                    }
                }
            })

            .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                @Override
                // Обработка длинного клика, например, только для SecondaryDrawerItem
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                    if (drawerItem instanceof SecondaryDrawerItem) {
                        Toast.makeText(activity, activity.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

    }

    @Override
    public Result build() {
        return super.build();
    }


}
