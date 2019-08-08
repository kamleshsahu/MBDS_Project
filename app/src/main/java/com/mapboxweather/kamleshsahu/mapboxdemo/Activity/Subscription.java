package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.util.IabBroadcastReceiver;
import com.mapboxweather.kamleshsahu.mapboxdemo.util.IabHelper;
import com.mapboxweather.kamleshsahu.mapboxdemo.util.IabResult;
import com.mapboxweather.kamleshsahu.mapboxdemo.util.Inventory;
import com.mapboxweather.kamleshsahu.mapboxdemo.util.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.trialy.library.Trialy;
import io.trialy.library.TrialyCallback;
import mehdi.sakout.fancybuttons.FancyButton;

import static android.view.View.GONE;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.BaseActivity.havetrial;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.SKU_INFINITE_GAS_HALFYEARLY;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.SKU_INFINITE_GAS_MONTHLY;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.SKU_INFINITE_GAS_QUATERLY;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.SKU_INFINITE_GAS_YEARLY;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.TRIALY_APP_KEY;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.TRIALY_SKU;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.base64EncodedPublicKey;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_ENDED;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_NOT_YET_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_OVER;
import static io.trialy.library.Constants.STATUS_TRIAL_RUNNING;

/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Subscription extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener,
        DialogInterface.OnClickListener {
    // Debug tag, for logging
    static final String TAG = "Subscription Activity";

    // Does the user have the premium upgrade?



    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    // Will the subscription auto-renew?
    boolean mAutoRenewEnabled = false;

    // Tracks the currently owned infinite gas SKU, and the options in the Manage dialog
    String mInfiniteGasSku = "";
    String mFirstChoiceSku = "";
    String mSecondChoiceSku = "";
    String mThirdChoiceSku="";
    String mFourthChoiceSku="";

    // Used to select between purchasing gas on a monthly or yearly basis
    String mSelectedSubscriptionPeriod = "";

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
//    static final String SKU_PREMIUM = "premium";
//    static final String SKU_GAS = "gas";

    // SKU for our subscription (infinite gas)
//    static final String SKU_INFINITE_GAS_MONTHLY = "infinite_gas_monthly";
//    static final String SKU_INFINITE_GAS_QUATERLY = "quaterly";
//    static final String SKU_INFINITE_GAS_HALFYEARLY = "halfyearly";
//    static final String SKU_INFINITE_GAS_YEARLY = "infinite_gas_yearly";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // Graphics for the gas gauge
//    static int[] TANK_RES_IDS = { R.drawable.gas0, R.drawable.gas1, R.drawable.gas2,
//            R.drawable.gas3, R.drawable.gas4 };

    // How many units (1/4 tank is our unit) fill in the tank.
//    static final int TANK_MAX = 4;

    // Current amount of gas in tank, in units
//    int mTank;

    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    Trialy mTrialy;

    Boolean buySubs=false;

//   String TRIALY_APP_KEY = "CNXFXUSWNXNREPZN6FW"; //TODO: Replace with your app key, which can be found on your Trialy developer dashboard
//   String TRIALY_SKU = "t2"; //TODO: Replace with a trial SKU, which can be found on your Trialy developer dashboard. Each app can have multiple trials
//   String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnh6LDOmwwPSQ8KesBlRQ/LrN/75xUFQhVmvfJG6uUlmgxU4iWiMzwr1iydveIz3cNT2C1IdnBpohHuDhn9RlOn5uaR3Cw0BDGrnRzwHZRPdoJ3/tAWIS+cLD/5LU7sriMOi6spMaPTYjgrT/Lck36goPwY88FK+e2G09cFrd54WQBPwHO+COKlKOFQ7Yt9yiCLlwivhdSDbacuVGg696JjAeTBvnw0eqks7Q/FHg2U0TlhBf/RU2+tvCnR2L0hk1kgkkdZFua8aDrZ1xQkEkBzlrrHrGnmqCyVoPHwMcxoOKM61BX511NMRuBJv9Eg19n4QITqT/fsR7vzmnljjxLQIDAQAB" ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subscription);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.sub_card).setBackgroundResource(R.drawable.gradient);

        mTrialy = new Trialy(this, TRIALY_APP_KEY);
        mTrialy.checkTrial(TRIALY_SKU, mTrialyCallback);

      //  LottieAnimationView lottieAnimationView=findViewById(R.id.sun);
        //lottieAnimationView.setSpeed((float) 0.5);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
            findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            }
        }, 000);
//        StartSmartAnimation.startAnimation( findViewById(R.id.toolbar) , AnimationType.SlideInDown , 700 , 0 , true );
//      //  StartSmartAnimation.startAnimation( findViewById(R.id.sun) , AnimationType.BounceInDown , 2000 , 0 , true );
//        StartSmartAnimation.startAnimation( findViewById(R.id.llTimeRemainingg) , AnimationType.ZoomInRubberBand , 700 , 0 , true );
//        StartSmartAnimation.startAnimation( findViewById(R.id.sub_box) , AnimationType.SlideInLeft , 700 , 0 , true );
//        StartSmartAnimation.startAnimation( findViewById(R.id.linearLayout3) , AnimationType.SlideInUp , 700 , 0 , true );
        // load game data
    //    loadData();

        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */


        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(Subscription.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });


        findViewById(R.id.btnStartTrial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrialy.startTrial(TRIALY_SKU, mTrialyCallback);
            }
        });

//
//        Toolbar back = (Toolbar) findViewById(R.id.toolbar);
//
//        back.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // back button pressed
//                startActivity(new Intent(Subscription.this,MainActivity.class));
//                finish();
//            }
//        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
//            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
//            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // First find out which subscription is auto renewing
            Purchase gasMonthly = inventory.getPurchase(SKU_INFINITE_GAS_MONTHLY);
            Purchase gasQuaterly = inventory.getPurchase(SKU_INFINITE_GAS_QUATERLY);
            Purchase gasHalfYearly = inventory.getPurchase(SKU_INFINITE_GAS_HALFYEARLY);
            Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
            if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_MONTHLY;
                mAutoRenewEnabled = true;
            }else if (gasQuaterly != null && gasQuaterly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_QUATERLY;
                mAutoRenewEnabled = true;
            }else if (gasHalfYearly!= null && gasHalfYearly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_HALFYEARLY;
                mAutoRenewEnabled = true;
            }
            else if (gasYearly != null && gasYearly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
                mAutoRenewEnabled = true;
            } else {
                mInfiniteGasSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
                    || (gasQuaterly != null && verifyDeveloperPayload(gasQuaterly))
                    || (gasHalfYearly != null && verifyDeveloperPayload(gasHalfYearly))
                    || (gasYearly != null && verifyDeveloperPayload(gasYearly));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas){
                FancyButton btnSubscribe=findViewById(R.id.subscribe);
                btnSubscribe.setText("Subscribed");
                btnSubscribe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnSubscribe.setTextColor(getResources().getColor(R.color.loo_pre));
                findViewById(R.id.btnStartTrial).setVisibility(GONE);
                findViewById(R.id.llTimeRemaining).setVisibility(View.VISIBLE);
                TextView tvTimeRemaining = (TextView)findViewById(R.id.tvTimeRemaining);
                tvTimeRemaining.setText(String.format(Locale.ENGLISH, "You are subscribed to %s pack", mInfiniteGasSku.split("_")[0]));
              //  (findViewById(R.id.llTimeRemaining)))
                   //     .setVisibility(GONE);
                }
 //               mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
//            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
//            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//                try {
//                    mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                }
//                return;
//            }

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    // User clicked the "Buy Gas" button
//    public void onBuyGasButtonClicked(View arg0) {
//        Log.d(TAG, "Buy gas button clicked.");
//
//        if (mSubscribedToInfiniteGas) {
//            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
//            return;
//        }
//
//        if (mTank >= TANK_MAX) {
//            complain("Your tank is full. Drive around a bit!");
//            return;
//        }
//
//        // launch the gas purchase UI flow.
//        // We will be notified of completion via mPurchaseFinishedListener
//        setWaitScreen(true);
//        Log.d(TAG, "Launching purchase flow for gas.");
//
//        /* TODO: for security, generate your payload here for verification. See the comments on
//         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//         *        an empty string, but on a production app you should carefully generate this. */
//        String payload = "";
//
//        try {
//            mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
//                    mPurchaseFinishedListener, payload);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error launching purchase flow. Another async operation in progress.");
//            setWaitScreen(false);
//        }
//    }

    // User clicked the "Upgrade to Premium" button.
//    public void onUpgradeAppButtonClicked(View arg0) {
//        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
//        setWaitScreen(true);
//
//        /* TODO: for security, generate your payload here for verification. See the comments on
//         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//         *        an empty string, but on a production app you should carefully generate this. */
//        String payload = "";
//
//        try {
//            mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
//                    mPurchaseFinishedListener, payload);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error launching purchase flow. Another async operation in progress.");
//            setWaitScreen(false);
//        }
//    }

    // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
    // flow for subscription.
    public void onInfiniteGasButtonClicked(View arg0) {
        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

        CharSequence[] options;
        if (!mSubscribedToInfiniteGas || !mAutoRenewEnabled) {
            // Both subscription options should be available
            options = new CharSequence[4];
            options[0] = getString(R.string.subscription_period_monthly);
            options[1] = getString(R.string.subscription_period_quaterly);
            options[2] = getString(R.string.subscription_period_halfyearly);
            options[3] = getString(R.string.subscription_period_yearly);
            mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
            mSecondChoiceSku = SKU_INFINITE_GAS_QUATERLY;
            mThirdChoiceSku=SKU_INFINITE_GAS_HALFYEARLY;
            mFourthChoiceSku=SKU_INFINITE_GAS_YEARLY;
        } else {
            // This is the subscription upgrade/downgrade path, so only one option is valid
            options = new CharSequence[3];
            if (mInfiniteGasSku.equals(SKU_INFINITE_GAS_MONTHLY)) {
                // Give the option to upgrade to yearly
                options[0] = getString(R.string.subscription_period_quaterly);
                options[1] = getString(R.string.subscription_period_halfyearly);
                options[2] = getString(R.string.subscription_period_yearly);
                mFirstChoiceSku = SKU_INFINITE_GAS_QUATERLY;
                mSecondChoiceSku = SKU_INFINITE_GAS_HALFYEARLY;
                mThirdChoiceSku=SKU_INFINITE_GAS_YEARLY;

            } else if(mInfiniteGasSku.equals(SKU_INFINITE_GAS_QUATERLY)){
                // Give the option to downgrade to monthly
                options[0] = getString(R.string.subscription_period_monthly);
                options[1] = getString(R.string.subscription_period_halfyearly);
                options[2] = getString(R.string.subscription_period_yearly);
                mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
                mSecondChoiceSku = SKU_INFINITE_GAS_HALFYEARLY;
                mThirdChoiceSku=SKU_INFINITE_GAS_YEARLY;
            } else if(mInfiniteGasSku.equals(SKU_INFINITE_GAS_HALFYEARLY)){
                // Give the option to downgrade to monthly
                options[0] = getString(R.string.subscription_period_monthly);
                options[1] = getString(R.string.subscription_period_quaterly);
                options[2] = getString(R.string.subscription_period_yearly);
                mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
                mSecondChoiceSku = SKU_INFINITE_GAS_QUATERLY;
                mThirdChoiceSku=SKU_INFINITE_GAS_YEARLY;
            }
            else if(mInfiniteGasSku.equals(SKU_INFINITE_GAS_YEARLY)){
                // Give the option to downgrade to monthly
                options[0] = getString(R.string.subscription_period_monthly);
                options[1] = getString(R.string.subscription_period_quaterly);
                options[2] = getString(R.string.subscription_period_halfyearly);
                mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
                mSecondChoiceSku = SKU_INFINITE_GAS_QUATERLY;
                mThirdChoiceSku=SKU_INFINITE_GAS_HALFYEARLY;
            }
        //    mSecondChoiceSku = "";
              mFourthChoiceSku = "";
        }

        int titleResId;
        if (!mSubscribedToInfiniteGas) {
            titleResId = R.string.subscription_period_prompt;
        } else if (!mAutoRenewEnabled) {
            titleResId = R.string.subscription_resignup_prompt;
        } else {
            titleResId = R.string.subscription_update_prompt;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleResId)
                .setSingleChoiceItems(options, 0 /* checkedItem */, this)
                .setPositiveButton(R.string.subscription_prompt_continue, this)
                .setNegativeButton(R.string.subscription_prompt_cancel, this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id == 0 /* First choice item */) {
            mSelectedSubscriptionPeriod = mFirstChoiceSku;
        } else if (id == 1 /* Second choice item */) {
            mSelectedSubscriptionPeriod = mSecondChoiceSku;
        }else if (id == 2 /* Second choice item */) {
            mSelectedSubscriptionPeriod = mThirdChoiceSku;
        }else if (id == 3 /* Second choice item */) {
            mSelectedSubscriptionPeriod = mFourthChoiceSku;
        } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
            /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
            String payload = "";

            if (TextUtils.isEmpty(mSelectedSubscriptionPeriod)) {
                // The user has not changed from the default selection
                mSelectedSubscriptionPeriod = mFirstChoiceSku;
            }

            List<String> oldSkus = null;
            if (!TextUtils.isEmpty(mInfiniteGasSku)
                    && !mInfiniteGasSku.equals(mSelectedSubscriptionPeriod)) {
                // The user currently has a valid subscription, any purchase action is going to
                // replace that subscription
                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            }

            setWaitScreen(true);
            Log.d(TAG, "Launching purchase flow for gas subscription.");
            try {
                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
                setWaitScreen(false);
            }
            // Reset the dialog options
            mSelectedSubscriptionPeriod = "";
            mFirstChoiceSku = "";
            mSecondChoiceSku = "";
            mThirdChoiceSku="";
            mFourthChoiceSku="";
        } else if (id != DialogInterface.BUTTON_NEGATIVE) {
            // There are only four buttons, this should not happen
            Log.e(TAG, "Unknown button clicked in subscription dialog: " + id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                if(result.getResponse()!= -1005){
                    complain("Error purchasing: " + result);
                }
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");


                if (purchase.getSku().equals(SKU_INFINITE_GAS_MONTHLY)
                        || purchase.getSku().equals(SKU_INFINITE_GAS_QUATERLY)
                        || purchase.getSku().equals(SKU_INFINITE_GAS_HALFYEARLY)
                    || purchase.getSku().equals(SKU_INFINITE_GAS_YEARLY)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to "+purchase.getSku().split("_")[0]+" pack!");
                mSubscribedToInfiniteGas = true;
                mAutoRenewEnabled = purchase.isAutoRenewing();
                mInfiniteGasSku = purchase.getSku();
//                mTank = TANK_MAX;
                    updateUi();
                setWaitScreen(false);
//
               FancyButton btnSubscribe=findViewById(R.id.subscribe);
               btnSubscribe.setText("Subscribed");
               btnSubscribe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               btnSubscribe.setTextColor(getResources().getColor(R.color.loo_pre));
                    findViewById(R.id.btnStartTrial).setVisibility(GONE);
                    findViewById(R.id.llTimeRemaining).setVisibility(View.VISIBLE);
                    TextView tvTimeRemaining = (TextView)findViewById(R.id.tvTimeRemaining);
                    tvTimeRemaining.setText(String.format(Locale.ENGLISH, "You are subscribed to %s pack",  purchase.getSku().split("_")[0]));
//                    startActivity(new Intent(Subscription.this,MapActivity.class));
//                    finish();

            }
        }
    };




    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    // updates UI to reflect model
    public void updateUi() {
        // update the car color to reflect premium status or lack thereof
//        ((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);
//
//        // "Upgrade" button is only visible if the user is not premium
//        findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);
//
//        ImageView infiniteGasButton = (ImageView) findViewById(R.id.infinite_gas_button);
//        if (mSubscribedToInfiniteGas) {
//            // If subscription is active, show "Manage Infinite Gas"
//            infiniteGasButton.setImageResource(R.drawable.manage_infinite_gas);
//        } else {
//            // The user does not have infinite gas, show "Get Infinite Gas"
//            infiniteGasButton.setImageResource(R.drawable.get_infinite_gas);
//        }

        // update gas gauge to reflect tank status
//        if (mSubscribedToInfiniteGas) {
//            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
//        }
//        else {
//            int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
//            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
//        }
    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
//        findViewById(R.id.screen_main).setVisibility(set ? GONE : View.VISIBLE);
//        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : GONE);
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();


    }

    private TrialyCallback mTrialyCallback = new TrialyCallback() {
        @Override
        public void onResult(int status, long timeRemaining, String sku) {
            switch (status){
                case STATUS_TRIAL_JUST_STARTED:
                    //The trial has just started - enable the premium features for the user
                    disableStartTrialButton("Trial Activated");
                    ((FancyButton)findViewById(R.id.btnStartTrial)).setTextColor(R.color.cardview_dark_background);
                    //Update the "Time remaining"-label
                    havetrial=true;
                    updateTimeRemainingLabel(timeRemaining);

                    //Optional: Show an informational dialog
//                    Intent intent=new Intent(Subscription.this,MapActivity.class);
//                    startActivity(intent);
 //                   int daysRemaining = Math.round(timeRemaining / (60 * 60 * 24));
 //                   showDialog("Trial started", String.format(Locale.ENGLISH, "You can now try the premium features for %d days",  daysRemaining), "OK");
                    break;
                case STATUS_TRIAL_RUNNING:
                    //The trial is currently running - enable the premium features for the user
                    updateTimeRemainingLabel(timeRemaining);
                    //Disable the "Start Trial" button
//                    Intent intent1=new Intent(Subscription.this,MapActivity.class);
//                    startActivity(intent1);
                    havetrial=true;
                    disableStartTrialButton("Trial Activated");
                    FancyButton btnStartTrial = findViewById(R.id.btnStartTrial);
                    btnStartTrial.setTextColor(getResources().getColor(R.color.cardview_dark_background));
                    btnStartTrial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case STATUS_TRIAL_JUST_ENDED:
                    //The trial has just ended - block access to the premium features
                    disableStartTrialButton("Trial Ended");
                    //Hide the "Time remaining"-label
                    updateTimeRemainingLabel(-1);
                    break;
                case STATUS_TRIAL_NOT_YET_STARTED:
                    //The user hasn't requested a trial yet - no need to do anything
                    break;
                case STATUS_TRIAL_OVER:
                    //The trial is over
                    disableStartTrialButton("Trial Ended");
                    break;
            }
            Log.i("TRIALY", "Returned status: " + Trialy.getStatusMessage(status));
        }

    };

    private void updateTimeRemainingLabel(long timeRemaining){
        if(timeRemaining == -1){
            //Hide the llTimeRemaining-LinearLayout
            LinearLayout llTimeRemaining = (LinearLayout)findViewById(R.id.llTimeRemaining);
            llTimeRemaining.setVisibility(GONE);
            return;
        }else  if(!mSubscribedToInfiniteGas) {
        //Convert the "timeRemaining"-value (in seconds) to days
        int daysRemaining = (int) timeRemaining / (60 * 60 * 24);
        //Update the tvTimeRemaining-TextView

            TextView tvTimeRemaining = (TextView) findViewById(R.id.tvTimeRemaining);
            tvTimeRemaining.setText(String.format(Locale.ENGLISH, "Your trial ends in %d days", daysRemaining));
            //Show the llTimeRemaining-LinearLayout
            LinearLayout llTimeRemaining = (LinearLayout) findViewById(R.id.llTimeRemaining);
            llTimeRemaining.setVisibility(View.VISIBLE);
        }
    }

    private void disableStartTrialButton(String text){
        FancyButton btnStartTrial = findViewById(R.id.btnStartTrial);
        btnStartTrial.setEnabled(false);
        btnStartTrial.setText(text);
    }

    private void showDialog(String title, String message, String buttonLabel){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(buttonLabel, null);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //Write your logic here
//                if(!mSubscribedToInfiniteGas && !havetrial) {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                    System.exit(0);
//
//                }
//                super.onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        if(!mSubscribedToInfiniteGas && !havetrial) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//            System.exit(0);
//
//            }
//        super.onBackPressed();
//    }


}
