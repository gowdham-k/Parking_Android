package com.polsec.pyrky.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.polsec.pyrky.R;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.Reports;
import com.polsec.pyrky.pojo.ratingval;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{
    private Context context;
    private String places[];
    private String dateTime[];
    private int[] currentRating;
    private int mPosition;
    private Boolean isPopUpShowing = false;
 int currenRating;
    int count = 0;
    String docid,uid,Rating,cameraid,lat,longi,bookingidval;
    RatingBar ratingBar;
           TextView ratingbar1,viewCar;
    FirebaseFirestore db;

    FirebaseAuth mAuth;
    String mUid;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1=new HashMap<>();
    List<Camera>CameraList = new ArrayList<Camera>();
    ArrayList<ratingval> vehList = new ArrayList<>();

    List<Booking> bookingList = new ArrayList<Booking>();
    String Datetime;

    HistoryRecyclerAdapter recyclerAdapter;
    RecyclerView mRecyclerView;
    public HistoryRecyclerAdapter(Context context, List<Booking> bookingList, Map<String, Object> bookingid1, List<Camera> cameraList, HistoryRecyclerAdapter recyclerAdapter, RecyclerView mRecyclerView) {
        this.context = context;
        this.bookingList = bookingList;
        this.bookingid1=bookingid1;
        this.CameraList=CameraList;
        this.recyclerAdapter=recyclerAdapter;
        this.mRecyclerView=mRecyclerView;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_booking_list, parent, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        docid=PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_DOCUMENTID);
//        Toast.makeText(context, docid, Toast.LENGTH_SHORT).show();


        //view.setOnClickListener(HomeActivity.myOnClickListener);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime;
        RelativeLayout Rating_lay;

        ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
            ratingBar = itemView.findViewById(R.id.history_ratings);
            ratingbar1 = itemView.findViewById(R.id.history_ratingsanother);
            Rating_lay=itemView.findViewById(R.id.view_car_text_lay);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mPosition = position;

        double time= Double.parseDouble(String.valueOf(bookingList.get(position).getDateTime()));
        int vali= (int) time;

        long dv = Long.valueOf(String.valueOf(vali))*1000;// its need to be in milisecond
        Date df = new Date(Long.valueOf((long) dv));
        Datetime= new SimpleDateFormat("dd MMM,  hh:mma").format(df);
        String str = Datetime.replace("AM", "am").replace("PM","pm");


//        Log.e("vv", String.valueOf(str));
        holder.dateTime.setText(str);


        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
        bookingidval=bookingList.get(position).getDocumentID();

        if(bookingid1.containsKey(bookingList.get(position).getDocumentID())){
            Boolean value=(Boolean) bookingid1.get(bookingList.get(position).getDocumentID());

            if(!value){
                holder.city.setText(bookingList.get(position).getDestName());
            }
            else {
                holder.itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
        }
//        }

        cameraid=bookingList.get(position).getCameraId();
        lat=bookingList.get(position).getDestLat();
        longi=bookingList.get(position).getDestLong();
        String latlong=lat+","+longi;
        ratingBar.setRating((int) bookingList.get(position).getParkingSpaceRating());
        ratingBar.setIsIndicator(true);

        ratingBar.setRating((float) bookingList.get(position).getParkingSpaceRating());
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#00B9AB"), PorterDuff.Mode.SRC_ATOP);

//        String ratval= String.valueOf(bookingList.get(position).getParkingSpaceRating());

        if(bookingList.get(position).getParkingSpaceRating()<=0){
            ratingBar.setVisibility(View.GONE);
            viewCar.setVisibility(View.GONE);
            ratingbar1.setVisibility(View.VISIBLE);
            ratingbar1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    String current = bookingList.get(position).getDocumentID();

                    showDialog(uid,cameraid,latlong,current,position);


//                showDialog(cameraid,uid,latlong);
                    return false;
                }
            });

        }
        else{

            ratingBar.setVisibility(View.VISIBLE);
            ratingbar1.setVisibility(View.GONE);
            viewCar.setVisibility(View.VISIBLE);
        }

//
    }

    private void showDialog(String uid, String cameraid, String latlongi, String current, int position) {

        Dialog dialog = new Dialog(context);
        LayoutInflater factory = LayoutInflater.from(context);
        View bottomSheetView = factory.inflate(R.layout.history_ratingalert, null);
        dialog.setContentView(bottomSheetView);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView Oktaxt = (TextView) dialog.findViewById(R.id.ok_txt);
        RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.rtbHighscr);
        TextView canceltxt = (TextView) dialog.findViewById(R.id.cancel_txt);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                currenRating = (int) v;

                ratingBar.setRating((int) currenRating);

                Toast.makeText(context, "New default rating: " + v, Toast.LENGTH_SHORT).show();
            }
        });

//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        Oktaxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> rating = new HashMap<>();
                rating.put("parkingSpaceRating", currenRating );
                Toast.makeText(context, "New default rating: " + currenRating, Toast.LENGTH_SHORT).show();


                db.collection("Bookings").document(current).update(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bookingList.get(position).getParkingSpaceRating();
                        bookingList.get(position).setParkingSpaceRating(currenRating);
                        swapItems(bookingList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                ratingval vehiclePojoObject=new ratingval();
                vehiclePojoObject.setUser_ID(uid);
                vehiclePojoObject.setRatings(String.valueOf(currenRating));

                vehList.add(vehiclePojoObject);

              Reports reports=new Reports(vehList,cameraid);

                db.collection("Reports").document(latlongi).set(reports).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                dialog.dismiss();

            }
        });

        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.dismiss();
            }
        });

    }


        public void showDialog(int currentRating, View v){
        isPopUpShowing = true;
        AlertDialog.Builder popDialog = new AlertDialog.Builder(context);

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        final RatingBar rating = new RatingBar(context);
        rating.setLayoutParams(lp);
        rating.setMax(5);
        rating.setNumStars(5);
        rating.setRating(currentRating);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(rating);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                currentRating[mPosition] = ( int ) v;
                System.out.println("Rated val:"+v);
            }
        });

        popDialog.setTitle("Rate the space");

        popDialog.setView(linearLayout);
        popDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int pos = getItemCount();
                if (pos != RecyclerView.NO_POSITION){

                }
                String stars = String.valueOf(rating.getRating());
                Toast.makeText(context, stars, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                isPopUpShowing = false;
            }
        });
        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                isPopUpShowing = false;
            }
        });
        popDialog.setCancelable(false);
        popDialog.create();
        popDialog.show();

    }
//

    public void swapItems(List<Booking> bookingList){
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }


}
