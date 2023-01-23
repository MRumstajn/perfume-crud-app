package rumstajn.parfem.parfem.view.list_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mauricio.parfem.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rumstajn.parfem.parfem.model.Perfume;

public class ListAdapter extends BaseAdapter {
    private List<Perfume> perfumes;

    public ListAdapter() {
        perfumes = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return perfumes.size();
    }

    @Override
    public Object getItem(int position) {
        return perfumes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return perfumes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.perfume_card, parent, false);
        }

        Perfume perfume = perfumes.get(position);

        ((TextView) convertView.findViewById(R.id.perfume_card_name)).setText(perfume.getName());
        ImageView bitmap = ((ImageView) convertView.findViewById(R.id.perfume_card_bitmap));

        if (perfume.getImagePath() != null && perfume.getImagePath().length() > 0) {
            Picasso.get().load(perfume.getImagePath()).into(bitmap);
        }

        return convertView;
    }

    public void setPerfumes(List<Perfume> perfumes) {
        this.perfumes = perfumes;
    }

    public List<Perfume> getPerfumes() {
        return perfumes;
    }

    public void addPerfume(Perfume perfume){
        if (!perfumes.contains(perfume)) {
            perfumes.add(perfume);
        }
    }
}
