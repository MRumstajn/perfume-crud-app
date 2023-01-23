package rumstajn.parfem.parfem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.model.dao.PerfumeDAO;
import rumstajn.parfem.parfem.model.dao.PerfumeDatabaseProvider;

public class PerfumeViewModel extends AndroidViewModel {
    private final PerfumeDAO perfumeDAO;

    private Perfume selectedPerfume;

    public PerfumeViewModel(@NonNull Application application) {
        super(application);

        perfumeDAO = PerfumeDatabaseProvider.getDatabaseInstance(application.getApplicationContext()).getPerfumeDAO();
    }

    public LiveData<List<Perfume>> getAllPerfumes() {
        return perfumeDAO.getAllPerfumes();
    }
    public void addNewPerfume(Perfume perfume){
        perfumeDAO.addNewPerfume(perfume);
    }

    public void editPerfume(Perfume perfume){
        perfumeDAO.editPerfume(perfume);
    }

    public void deletePerfume(Perfume perfume){
        perfumeDAO.deletePerfume(perfume);
    }

    public void setSelectedPerfume(Perfume selectedPerfume) {
        this.selectedPerfume = selectedPerfume;
    }

    public Perfume getSelectedPerfume() {
        return selectedPerfume;
    }
}
