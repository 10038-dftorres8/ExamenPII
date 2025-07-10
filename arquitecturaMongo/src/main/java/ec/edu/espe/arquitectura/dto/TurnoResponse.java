package ec.edu.espe.arquitectura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de información del turno")
public class TurnoResponse {
    
    @Schema(description = "ID único del turno", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Código de la caja", example = "CAJ01")
    private String codigoCaja;
    
    @Schema(description = "Código del cajero", example = "USU01")
    private String codigoCajero;
    
    @Schema(description = "Código único del turno", example = "CAJ01-USU01-20250709")
    private String codigoTurno;
    
    @Schema(description = "Fecha y hora de inicio del turno", example = "2025-07-09T08:00:00")
    private LocalDateTime inicioTurno;
    
    @Schema(description = "Fecha y hora de fin del turno", example = "2025-07-09T17:00:00")
    private LocalDateTime finTurno;
    
    @Schema(description = "Monto inicial del turno", example = "5000.00")
    private BigDecimal montoInicial;
    
    @Schema(description = "Monto final del turno", example = "6500.00")
    private BigDecimal montoFinal;
    
    @Schema(description = "Monto calculado según transacciones", example = "6500.00")
    private BigDecimal montoCalculado;
    
    @Schema(description = "Diferencia entre monto final y calculado", example = "0.00")
    private BigDecimal diferencia;
    
    @Schema(description = "Estado del turno", example = "ABIERTO", allowableValues = {"ABIERTO", "CERRADO", "SIN_TURNO"})
    private String estado;
    
    @Schema(description = "Mensaje informativo", example = "Turno abierto exitosamente")
    private String mensaje;
} 