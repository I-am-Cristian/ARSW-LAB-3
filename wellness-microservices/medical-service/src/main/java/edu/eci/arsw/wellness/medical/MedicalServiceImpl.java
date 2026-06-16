package edu.eci.arsw.wellness.medical;

import edu.eci.arsw.wellness.proto.medical.*;
import io.grpc.stub.StreamObserver;
import java.util.*;

public class MedicalServiceImpl extends MedicalServiceGrpc.MedicalServiceImplBase {
    
    private final Map<String, SpecialtyInfo> specialties = new HashMap<>();
    private final Map<String, List<DoctorInfo>> doctorsBySpecialty = new HashMap<>();
    
    public MedicalServiceImpl() {
        specialties.put("Medicina General", SpecialtyInfo.newBuilder()
            .setName("Medicina General")
            .setDescription("Atención médica primaria, chequeos generales y diagnóstico de enfermedades comunes")
            .setAvailableDoctors(3)
            .addAllCommonTreatments(Arrays.asList("Chequeo general", "Control de signos vitales", "Vacunación"))
            .build());
        
        specialties.put("Psicología", SpecialtyInfo.newBuilder()
            .setName("Psicología")
            .setDescription("Apoyo psicológico, terapia individual y manejo de estrés académico")
            .setAvailableDoctors(2)
            .addAllCommonTreatments(Arrays.asList("Terapia individual", "Manejo de ansiedad", "Orientación vocacional"))
            .build());
        
        specialties.put("Odontología", SpecialtyInfo.newBuilder()
            .setName("Odontología")
            .setDescription("Cuidado dental, limpiezas y tratamientos de caries")
            .setAvailableDoctors(2)
            .addAllCommonTreatments(Arrays.asList("Limpieza dental", "Tratamiento de caries", "Extracciones"))
            .build());
        
        List<DoctorInfo> generalDoctors = Arrays.asList(
            DoctorInfo.newBuilder().setId("D001").setName("Dra. María Rodríguez")
                .setSpecialty("Medicina General").setSchedule("Lun-Vie 8:00-12:00").setAvailable(true).build(),
            DoctorInfo.newBuilder().setId("D002").setName("Dr. Carlos Martínez")
                .setSpecialty("Medicina General").setSchedule("Lun-Vie 14:00-18:00").setAvailable(true).build()
        );
        
        List<DoctorInfo> psychologyDoctors = Arrays.asList(
            DoctorInfo.newBuilder().setId("D003").setName("Dra. Ana Pérez")
                .setSpecialty("Psicología").setSchedule("Lun-Mie 9:00-13:00").setAvailable(true).build()
        );
        
        List<DoctorInfo> dentistryDoctors = Arrays.asList(
            DoctorInfo.newBuilder().setId("D004").setName("Dr. Juan López")
                .setSpecialty("Odontología").setSchedule("Mar-Jue 10:00-16:00").setAvailable(true).build()
        );
        
        doctorsBySpecialty.put("Medicina General", generalDoctors);
        doctorsBySpecialty.put("Psicología", psychologyDoctors);
        doctorsBySpecialty.put("Odontología", dentistryDoctors);
    }
    
    @Override
    public void getSpecialties(EmptyRequest request,
                               StreamObserver<SpecialtyList> responseObserver) {
        
        SpecialtyList response = SpecialtyList.newBuilder()
            .addAllSpecialties(specialties.values())
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getSpecialtyInfo(SpecialtyRequest request,
                                 StreamObserver<SpecialtyInfo> responseObserver) {
        
        SpecialtyInfo specialty = specialties.get(request.getSpecialtyName());
        
        if (specialty == null) {
            specialty = SpecialtyInfo.newBuilder()
                .setName(request.getSpecialtyName())
                .setDescription("Especialidad no encontrada")
                .setAvailableDoctors(0)
                .build();
        }
        
        responseObserver.onNext(specialty);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAvailableDoctors(SpecialtyRequest request,
                                    StreamObserver<DoctorList> responseObserver) {
        
        List<DoctorInfo> doctors = doctorsBySpecialty.getOrDefault(request.getSpecialtyName(), new ArrayList<>());
        
        DoctorList response = DoctorList.newBuilder()
            .addAllDoctors(doctors)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}