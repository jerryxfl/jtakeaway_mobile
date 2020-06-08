//package com.jerry.jtakeaway.ui.generalActivity.fragment;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//
//import com.jerry.jtakeaway.R;
//
//import io.flutter.embedding.android.FlutterFragment;
//import io.flutter.embedding.android.RenderMode;
//import io.flutter.embedding.android.TransparencyMode;
//
//public class TemplateFragment extends FragmentActivity {
//
//    // Define a tag String to represent the FlutterFragment within this
//    // Activity's FragmentManager. This value can be whatever you'd like.
//    private static final String TAG_FLUTTER_FRAGMENT = "flutter_fragment";
//
//    // Declare a local variable to reference the FlutterFragment so that you
//    // can forward calls to it later.
//    private FlutterFragment flutterFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.fragment_template);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        flutterFragment = (FlutterFragment) fragmentManager
//                .findFragmentByTag(TAG_FLUTTER_FRAGMENT);
//
//        if (flutterFragment == null) {
//            flutterFragment = FlutterFragment.createDefault();
//            fragmentManager
//                    .beginTransaction()
//                    .add(
//                            R.id.container,
//                            flutterFragment,
//                            TAG_FLUTTER_FRAGMENT
//                    )
//                    .commit();
//            FlutterFragment.withNewEngine()
//                    .initialRoute("/home")
//                    .renderMode(RenderMode.surface)
//                    .transparencyMode(TransparencyMode.transparent)
//                    .shouldAttachEngineToActivity(false)
//                    .build();
//        }
//    }
//
//
//    @Override
//    public void onPostResume() {
//        super.onPostResume();
//        flutterFragment.onPostResume();
//    }
//
//    @Override
//    protected void onNewIntent(@NonNull Intent intent) {
//        super.onNewIntent(intent);
//        flutterFragment.onNewIntent(intent);
//    }
//
//    @Override
//    public void onBackPressed() {
//        flutterFragment.onBackPressed();
//    }
//
//
//    @Override
//    public void onUserLeaveHint() {
//        flutterFragment.onUserLeaveHint();
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        flutterFragment.onTrimMemory(level);
//    }
//}