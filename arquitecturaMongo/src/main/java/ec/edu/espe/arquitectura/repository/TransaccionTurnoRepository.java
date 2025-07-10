package ec.edu.espe.arquitectura.repository;

import ec.edu.espe.arquitectura.entity.TransaccionTurno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransaccionTurnoRepository extends MongoRepository<TransaccionTurno, String> {
    
    List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
    
    List<TransaccionTurno> findByTipoTransaccion(String tipoTransaccion);
    
    List<TransaccionTurno> findByCodigoTurnoAndTipoTransaccion(String codigoTurno, String tipoTransaccion);
    
    List<TransaccionTurno> findByCodigoTurnoAndTipoTransaccionIn(String codigoTurno, List<String> tipos);
    
    List<TransaccionTurno> findByMontoTotalGreaterThan(BigDecimal monto);
    
    List<TransaccionTurno> findByMontoTotalLessThan(BigDecimal monto);
    
    List<TransaccionTurno> findByMontoTotalBetween(BigDecimal montoMin, BigDecimal montoMax);
    
    long countByCodigoTurno(String codigoTurno);
    
    long countByCodigoTurnoAndTipoTransaccion(String codigoTurno, String tipoTransaccion);
    
    boolean existsByCodigoTurnoAndTipoTransaccion(String codigoTurno, String tipoTransaccion);
    
    List<TransaccionTurno> findByCodigoTurnoIn(List<String> codigosTurno);
} 