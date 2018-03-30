package ng.org.knowit.med_manager.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.R;

/**
 * Created by john on 3/29/18.
 */

public class MedicineDatabaseAdapter extends RecyclerView.Adapter<MedicineDatabaseAdapter.MedicineDatabaseViewHolder> {

    private Context mContext;
    private Cursor mCursor;

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
        if (!mCursor.moveToPosition(position))
            return;
        String medicineName = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME));

        String medicineDescription = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DESCRIPTION));

        String medicineFrequency = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_FREQUENCY));
        String medicineDuration = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DURATION));

        String medicineTimeStamp = mCursor.getString(mCursor.getColumnIndex(
                MedicineContract.MedicineEntry.COLUMN_TIMESTAMP));


        medicineDatabaseViewHolder.medicineNameTextView.setText(medicineName);
        medicineDatabaseViewHolder.medicineDescriptionTextView.setText(medicineDescription);
        medicineDatabaseViewHolder.medicineFrequencyTextView.setText(medicineFrequency);
        medicineDatabaseViewHolder.medicineDurationTextView.setText(medicineDuration);
        medicineDatabaseViewHolder.medicineTimeStampTextView.setText(medicineTimeStamp);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    class MedicineDatabaseViewHolder extends RecyclerView.ViewHolder{
        TextView medicineNameTextView, medicineDescriptionTextView, medicineFrequencyTextView,
                medicineDurationTextView, medicineTimeStampTextView;

        public MedicineDatabaseViewHolder(View itemView) {
            super(itemView);
            medicineNameTextView = itemView.findViewById(R.id.text_medicine_name);
            medicineDescriptionTextView = itemView.findViewById(R.id.text_medicine_description);
            medicineFrequencyTextView = itemView.findViewById(R.id.text_frequency);
            medicineDurationTextView = itemView.findViewById(R.id.text_duration);
            medicineTimeStampTextView = itemView.findViewById(R.id.text_date_created);
        }
    }

}
