package ec.edu.espe.arquitectura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para procesar una transacción")
public class ProcesarTransaccionRequest {
    
    @Schema(description = "Código de la caja", example = "CAJ01", required = true)
    private String codigoCaja;
    
    @Schema(description = "Código del cajero", example = "USU01", required = true)
    private String codigoCajero;
    
    @Schema(description = "Tipo de transacción", example = "DEPOSITO", allowableValues = {"DEPOSITO", "RETIRO"}, required = true)
    private String tipoTransaccion;
    
    @Schema(description = "Monto total de la transacción", example = "1500.00", required = true)
    private BigDecimal montoTotal;
    
    @Schema(description = "Lista de denominaciones de la transacción", required = true)
    private List<DenominacionDto> denominaciones;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Denominación de billetes")
    public static class DenominacionDto {
        @Schema(description = "Valor del billete", example = "100", required = true)
        private Integer billete;
        
        @Schema(description = "Cantidad de billetes", example = "15", required = true)
        private Integer cantidadBilletes;
        
        @Schema(description = "Monto total de esta denominación", example = "1500.00", required = true)
        private BigDecimal monto;
    }
} 