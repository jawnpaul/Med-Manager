/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ng.org.knowit.med_manager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ng.org.knowit.med_manager.Activity.MedicineDetailActivity;
import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.R;

public class MedicineDatabaseAdapter extends RecyclerView.Adapter<MedicineDatabaseAdapter.MedicineDatabaseViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    private String MedicineName, MedicineDescription,
            MedicineFrequency, MedicineDuration, MedicineTimeStamp;

    private long Id;
    private int imageId;

    private final int[] medicineImages = new int[]{
            R.drawable.image_drug, R.drawable.image_drug_two, R.drawable.image_bp, R.drawable.image_one,
            R.drawable.image_two, R.drawable.image_three, R.drawable.image_four,
            R.drawable.image_five, R.drawable.image_seven, R.drawable.image_nine
    };

    public MedicineDatabaseAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public MedicineDatabaseViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
       View view = layoutInflater.inflate(R.layout.medicine_card, parent, false);
        return new MedicineDatabaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            MedicineDatabaseViewHolder medicineDatabaseViewHolder, int position) {

        int position1 = medicineDatabaseViewHolder.getAdapterPosition() + 1;
        setImage(position1);

        if (!mCursor.moveToPosition(position))
            return;
        String medicineName = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME));
        MedicineName = medicineName;


        String medicineDescription = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DESCRIPTION));
        MedicineDescription = medicineDescription;

        String medicineFrequency = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_FREQUENCY));
        MedicineFrequency = medicineFrequency;

        String medicineDuration = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DURATION));
        MedicineDuration = medicineDuration;

        String medicineTimeStamp = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_TIMESTAMP));
        MedicineTimeStamp = medicineTimeStamp;

        long id = mCursor.getLong(mCursor.getColumnIndex(MedicineContract.MedicineEntry._ID));

        medicineDatabaseViewHolder.itemView.setTag(id);

        Id = id;



        medicineDatabaseViewHolder.medicineNameTextView.setText(medicineName);
        medicineDatabaseViewHolder.medicineDescriptionTextView.setText(medicineDescription);
        medicineDatabaseViewHolder.medicineFrequencyTextView.setText(medicineFrequency);
        medicineDatabaseViewHolder.medicineDurationTextView.setText(medicineDuration);
        medicineDatabaseViewHolder.medicineTimeStampTextView.setText(medicineTimeStamp);
        medicineDatabaseViewHolder.medicineImageView.setImageResource(imageId);

        medicineDatabaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntentForMedicineDetail();
            }
        });

    }

    @Override
    public int getItemCount() {
        @SuppressWarnings("UnusedAssignment") int elementsCount = mCursor.getCount() + 1;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    class MedicineDatabaseViewHolder extends RecyclerView.ViewHolder{
        final TextView medicineNameTextView;
        final TextView medicineDescriptionTextView;
        final TextView medicineFrequencyTextView;
        final TextView medicineDurationTextView;
        final TextView medicineTimeStampTextView;
        final ImageView medicineImageView;

        public MedicineDatabaseViewHolder(View itemView) {
            super(itemView);
            medicineNameTextView = itemView.findViewById(R.id.text_medicine_name);
            medicineDescriptionTextView = itemView.findViewById(R.id.text_medicine_description);
            medicineFrequencyTextView = itemView.findViewById(R.id.text_frequency);
            medicineDurationTextView = itemView.findViewById(R.id.text_duration);
            medicineTimeStampTextView = itemView.findViewById(R.id.text_date_created);
            medicineImageView = itemView.findViewById(R.id.medicine_image);
        }
    }

    private void sendIntentForMedicineDetail(){

        Bundle bundle = new Bundle();
        bundle.putString("medicineName", MedicineName);
        bundle.putString("medicineDescription", MedicineDescription);
        bundle.putString("medicineFrequency", MedicineFrequency);
        bundle.putString("medicineDuration", MedicineDuration);
        bundle.putString("medicineTimeStamp", MedicineTimeStamp);
        bundle.putInt("imageId", imageId);
        bundle.putLong("id", Id);

        Intent intent = new Intent(mContext, MedicineDetailActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }

    private void setImage(int elementPosition){
        if (elementPosition-1<medicineImages.length){
            switch (elementPosition-1){
                case 0:
                    imageId = medicineImages[0];
                    break;
                case 1:
                    imageId = medicineImages[1];
                    break;
                case 2:
                    imageId = medicineImages[2];
                    break;
                case 3:
                    imageId = medicineImages[3];
                    break;
                case 4:
                    imageId = medicineImages[4];
                    break;
                case 5:
                    imageId = medicineImages[5];
                    break;
                case 6:
                    imageId = medicineImages[6];
                    break;
                case 7:
                    imageId = medicineImages[7];
                    break;
                case 8:
                    imageId = medicineImages[8];
                    break;
                case 9:
                    imageId = medicineImages[9];
                    break;
                default:
                    break;
            }
        }else if(elementPosition-1>=medicineImages.length){

            switch (elementPosition-1 % medicineImages.length){
                case 0:
                    imageId = medicineImages[0];
                    break;
                case 1:
                    imageId = medicineImages[1];
                    break;
                default:
                    break;
            }


        }

    }

}
