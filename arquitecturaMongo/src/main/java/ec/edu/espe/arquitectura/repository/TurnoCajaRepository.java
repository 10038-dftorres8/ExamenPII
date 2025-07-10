package ec.edu.espe.arquitectura.repository;

import ec.edu.espe.arquitectura.entity.TurnoCaja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoCajaRepository extends MongoRepository<TurnoCaja, String> {
    
    Optional<TurnoCaja> findByCodigoTurno(String codigoTurno);
    
    List<TurnoCaja> findByCodigoCaja(String codigoCaja);
    
    List<TurnoCaja> findByCodigoCajero(String codigoCajero);
    
    List<TurnoCaja> findByEstado(String estado);
    
    List<TurnoCaja> findByCodigoCajaAndEstado(String codigoCaja, String estado);
    
    List<TurnoCaja> findByCodigoCajeroAndEstado(String codigoCajero, String estado);
    
    List<TurnoCaja> findByInicioTurnoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<TurnoCaja> findByCodigoCajaAndInicioTurnoBetween(String codigoCaja, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    boolean existsByCodigoCajaAndEstado(String codigoCaja, String estado);
    
    boolean existsByCodigoCajeroAndEstado(String codigoCajero, String estado);
    
    List<TurnoCaja> findByDiferenciaNot(BigDecimal diferencia);
    
    List<TurnoCaja> findByCodigoCajaAndDiferenciaNot(String codigoCaja, BigDecimal diferencia);
} 