package ec.edu.espe.arquitectura.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Document(collection = "turnosCaja")
public class TurnoCaja {
    
    @Id
    private String id;
    
    private String codigoCaja;
    
    private String codigoCajero;
    
    private String codigoTurno;
    
    private LocalDateTime inicioTurno;
    
    private LocalDateTime finTurno;
    
    private BigDecimal montoInicial;
    
    private BigDecimal montoFinal;
    
    private BigDecimal montoCalculado;
    
    private BigDecimal diferencia;
    
    private String estado; // "ABIERTO" | "CERRADO"
} 