package ec.edu.espe.arquitectura.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Document(collection = "transaccionesTurno")
public class TransaccionTurno {
    
    @Id
    private String id;
    
    private String codigoTurno;
    
    private String tipoTransaccion; // "INICIO" | "DEPOSITO" | "RETIRO" | "CIERRE"
    
    private BigDecimal montoTotal;
    
    private List<Denominacion> denominaciones;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class Denominacion {
        private Integer billete;
        private Integer cantidadBilletes;
        private BigDecimal monto;
    }
} 