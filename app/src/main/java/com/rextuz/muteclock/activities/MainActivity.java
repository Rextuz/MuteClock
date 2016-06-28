package com.rextuz.muteclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rextuz.muteclock.R;
import com.rextuz.muteclock.rules.RuleManager;
import com.rextuz.muteclock.rules.RulesBaseAdapter;

public class MainActivity extends AppCompatActivity {

    public static RuleManager ruleManager;
    private RulesBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        ruleManager = new RuleManager(getApplicationContext());
        adapter = new RulesBaseAdapter(getApplicationContext(),
                ruleManager.rules);
        ListView ruleList = (ListView) findViewById(R.id.ruleList);
        ruleList.setAdapter(adapter);
        ruleManager.load();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            ruleManager.save();
            adapter.notifyDataSetChanged();
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), R.string.wrong_interval,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ruleManager.save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_rule:
                intent = new Intent(this, AddActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.action_delete_rule:
                for (int i = 0; i < ruleManager.size(); i++) {
                    if (RulesBaseAdapter.rulesToDelete.get(i)) {
                        ruleManager.removeRule(i);
                        RulesBaseAdapter.rulesToDelete.remove(i);
                        i--;
                    }
                }
                ruleManager.save();
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
