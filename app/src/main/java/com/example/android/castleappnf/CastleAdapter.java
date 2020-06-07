package com.example.android.castleappnf;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CastleAdapter extends RecyclerView.Adapter<CastleAdapter.CastleViewHolder> {

    private Context mContext;
    //Collection of Castles to populated the recyclerview with
    private ArrayList<Castles> castles;
    //Click listener which is implemented in MainActivity.
    private OnRecyclerItemClickListener mListener;
    //Location of the phone which is set from MainActivity when Location changes by 1 KM at least or when app starts
    private Location phoneLocation;
    //Shared preference info from MainActivity.
    private String distanceUnit;
    private String sortBy;
    private String filterBy;
    //FireBase storage reference
    private StorageReference storageReference;

    private static final String TAG = "CastleAdapter";



    public CastleAdapter() {
        //Empty constructor
    }

    //Constructor
    public CastleAdapter(Context mContext, ArrayList<Castles> castlesParam, OnRecyclerItemClickListener listener, String distance, String sort, StorageReference ref, String filter) {
        this.mContext = mContext;
        //The shared preference data from MainActivity
        this.distanceUnit = distance;
        this.sortBy = sort;
        filterBy = filter;
        //Filters the collection of Castles if the user selected to filter the Castles.
        //May have to add more filter options here if more filter options added.
        if (filterBy.equals("English Heritage")) {
            this.castles = filterCastles(castlesParam);
        } else {
            this.castles = castlesParam;
        }
        this.mListener = listener;
        //Receives the Firebase Storage reference in order to get the images
        storageReference = ref;


    }

    //Getter for phone location
    public Location getPhoneLocation() {
        return phoneLocation;
    }

    //Setter for phone location so it can be set (when it changes) in adapter from Mainactivity without recreated whole adapter (as would have to do if sent location in constructor)
    public void setPhoneLocation(Location phoneLocation) {
        this.phoneLocation = phoneLocation;

    }

    //Viewholder is created. Different layout for the items used if device has over a certain width in px.
    @NonNull
    @Override
    public CastleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        if (Resources.getSystem().getDisplayMetrics().widthPixels > 1000) {
            view = inflater.inflate(R.layout.large_list_item_layout, parent, false);
        }

        //Sorts the Castles collection based on what the sortBy value is in sharedpreferences in MainActivity
        if (phoneLocation != null && sortBy.equals("Distance")) {
            sortCastlesByDistance();
        } else if (sortBy.equals("A-Z")) {
            sortCastlesByAZ();
        } else if (sortBy.equals("Rating")) {
            sortCastlesByRating();
        }

        return new CastleViewHolder(view, mListener);
    }

    //Method which filters the Castles collection to return a new collection with only English Heritage Castles if the user wants to filter by this.
    //In future, may need to pass String that is the filter or even two strings into this method like filterCastles(collection of castles, string of castle variable to filter by, string of what to filter)
    //So filterCastles(collection, "Operator", "National Trust") would filter by national trust sites only.
    private ArrayList<Castles> filterCastles(ArrayList<Castles> x) {
        ArrayList<Castles> newList = new ArrayList<>();
        for (Castles c : x) {
            if (c.getOperator().equals("English Heritage")) {
                newList.add(c);
            }
        }
        Log.d(TAG, "filterCastles: " + newList);
        return newList;

    }

    //Uses the Castles comparator to sort castles by rating
    private void sortCastlesByRating() {
        Collections.sort(castles, new Castles.RatingComparator());
    }
    //Uses the Castles comparator to sort castles by distance. Has to use phone location to set the distances in each Castles object before this.
    private void sortCastlesByDistance() {
        for (Castles c : castles) {
            float dist = 0;
            Location castLocation = new Location("");
            castLocation.setLongitude(c.getLongdi());
            castLocation.setLatitude(c.getLat());
            dist = phoneLocation.distanceTo(castLocation);
            c.setDistance(dist);
        }
        Collections.sort(castles, new Castles.DistanceComparator());
    }
    //Uses the Castles comparator to sort castles by A-Z
    private void sortCastlesByAZ() {
        Collections.sort(castles, new Castles.AZComparator());
    }


    @Override
    public void onBindViewHolder(@NonNull final CastleViewHolder holder, final int position) {
        //The animation to bind the viewholder with.
        int animToUse = R.anim.slide_in_left;
        //Set a dist float to 0. This will be changed to have the distance between the phone and the castle in it
        float dist = 0;
        Location castLocation = new Location("");
        castLocation.setLongitude(castles.get(position).getLongdi());
        castLocation.setLatitude(castles.get(position).getLat());
        if (phoneLocation != null) {
            dist = phoneLocation.distanceTo(castLocation);
        }
        //Calls the viewholder bind method, passing in the name of the castle, distance, the distance unit, the rating, the context and the animation to use.
        holder.bind(castles.get(position).getName(), dist, distanceUnit, castles.get(position).getRating(), mContext, animToUse);

        //The image is obtained from the firebase storage reference using the castle name to get the correct image
        StorageReference myImage = storageReference.child("images/" + castles.get(position).getName().toLowerCase() + " 1.PNG");
        Log.d(TAG, "onBindViewHolder: " + myImage);
        myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //The viewholder method to the set the image to the viewholder image view is called, passing the uri of the image
                holder.setImage(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //If image load failed then null is passed to the viewholder and handled there.
                e.printStackTrace();
                holder.setImage(null);
            }
        });
    }

    //Returns the size of the collection
    @Override
    public int getItemCount() {
        return castles.size();
    }

    public class CastleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Viewholder UI elements
        TextView nameTextView;
        TextView distanceTextView;
        ImageView imgView;
        RatingBar mRatingBar;
        ConstraintLayout mConstraint;
        //Where to send the click events
        OnRecyclerItemClickListener onRecyclerItemClickListener;


        public CastleViewHolder(@NonNull View v, OnRecyclerItemClickListener listener) {
            super(v);
            nameTextView = v.findViewById(R.id.castleNameTextView);
            distanceTextView = v.findViewById(R.id.distanceValueTextView);
            imgView = v.findViewById(R.id.castleRvImageView);
            mRatingBar = v.findViewById(R.id.myRatingBar);
            mConstraint = v.findViewById(R.id.itemview_parent);

            onRecyclerItemClickListener = listener;
            //Sets the onclicklistener to this so it must implement onclick
            v.setOnClickListener(this);

        }

        public void bind(String x, float z, String distUnit, int rating, Context context, int theAnim) {
            //applies the anim to the whole viewholder
            mConstraint.setAnimation(AnimationUtils.loadAnimation(context, theAnim));
            //Sets the views in the viewholder.
            nameTextView.setText(x);
            mRatingBar.setRating(rating);
            if (distUnit.equals("Km")) {
                int myDist = (int) z / 1000;
                distanceTextView.setText(String.valueOf(myDist) + context.getString(R.string.km_away_string));
            } else {
                int myDist = (int) z / 1609;
                distanceTextView.setText(String.valueOf(myDist) + context.getString(R.string.miles_away_string));
            }
        }

        //Sets the imageview to the image obtained from firebase. If null is passed then firebase could not obtain the image and an error image is shown instead.
        public void setImage(Uri y) {
            Log.d(TAG, "bind: " + y);
            if (y != null) {
                Picasso.get().load(y).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(450, 310).centerInside().into(imgView);
            } else {
                Picasso.get().load(R.drawable.ic_error).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(450, 310).centerInside().into(imgView);

            }
        }
        //When the viewholder is clicked it triggers this method. This essentially sends the click to be handled by the Activity, which is passed to the adapter and then
        //to the viewholder. This activity implements this onmyitemclicked method which is an interface defined in the adapter. The mainactivity then responds to this
        //click how it wants. It knows which item is clicked as this method sends a parameter which is the Castles object clicked.
        @Override
        public void onClick(View view) {
            onRecyclerItemClickListener.onMyItemClicked(castles.get(getAdapterPosition()));
        }
    }
    //Interface to handle the clicks which is implemented in the host activity.
    public interface OnRecyclerItemClickListener{
        void onMyItemClicked(Castles c);
    }
}
