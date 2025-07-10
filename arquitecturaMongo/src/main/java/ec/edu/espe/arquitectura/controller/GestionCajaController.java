package ec.edu.espe.arquitectura.controller;

import ec.edu.espe.arquitectura.dto.AbrirTurnoRequest;
import ec.edu.espe.arquitectura.dto.ProcesarTransaccionRequest;
import ec.edu.espe.arquitectura.dto.CerrarTurnoRequest;
import ec.edu.espe.arquitectura.dto.TurnoResponse;
import ec.edu.espe.arquitectura.service.GestionCajaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventanillas")
@RequiredArgsConstructor
@Tag(name = "Gestión de Ventanillas", description = "API para gestión de turnos de caja, transacciones y cierre de ventanillas")
public class GestionCajaController {

    private final GestionCajaService gestionCajaService;

    @PostMapping("/turnos/abrir")
    @Operation(
        summary = "Abrir turno de caja",
        description = "Permite a un cajero abrir un nuevo turno registrando el dinero recibido de la bóveda con sus denominaciones"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Turno abierto exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación - Turno ya abierto o montos no coinciden"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TurnoResponse> abrirTurno(
            @Parameter(description = "Datos para abrir el turno", required = true)
            @RequestBody AbrirTurnoRequest request) {
        TurnoResponse response = gestionCajaService.abrirTurno(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transacciones/procesar")
    @Operation(
        summary = "Procesar transacción",
        description = "Permite procesar transacciones de depósito o retiro con registro de denominaciones"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacción procesada exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación - No hay turno abierto o montos no coinciden"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TurnoResponse> procesarTransaccion(
            @Parameter(description = "Datos de la transacción a procesar", required = true)
            @RequestBody ProcesarTransaccionRequest request) {
        TurnoResponse response = gestionCajaService.procesarTransaccion(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/turnos/cerrar")
    @Operation(
        summary = "Cerrar turno de caja",
        description = "Permite cerrar el turno validando las denominaciones finales y calculando diferencias"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno cerrado exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación - Diferencia detectada o no hay turno abierto"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TurnoResponse> cerrarTurno(
            @Parameter(description = "Datos para cerrar el turno", required = true)
            @RequestBody CerrarTurnoRequest request) {
        TurnoResponse response = gestionCajaService.cerrarTurno(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/turnos/estado")
    @Operation(
        summary = "Consultar estado del turno",
        description = "Permite consultar el estado actual de un turno por caja y cajero"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del turno consultado exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoResponse.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró turno abierto")
    })
    public ResponseEntity<TurnoResponse> obtenerEstadoTurno(
            @Parameter(description = "Código de la caja", required = true, example = "CAJ01")
            @RequestParam String codigoCaja,
            @Parameter(description = "Código del cajero", required = true, example = "USU01")
            @RequestParam String codigoCajero) {
        TurnoResponse response = gestionCajaService.obtenerEstadoTurno(codigoCaja, codigoCajero);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/turnos/historial/{codigoCaja}")
    @Operation(
        summary = "Obtener historial de turnos",
        description = "Permite consultar el historial completo de turnos de una caja específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial consultado exitosamente",
            content = @Content(schema = @Schema(implementation = TurnoResponse.class)))
    })
    public ResponseEntity<List<TurnoResponse>> obtenerHistorialTurnos(
            @Parameter(description = "Código de la caja", required = true, example = "CAJ01")
            @PathVariable String codigoCaja) {
        List<TurnoResponse> response = gestionCajaService.obtenerHistorialTurnos(codigoCaja);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/turnos/abiertos/{codigoCaja}")
    @Operation(
        summary = "Consultar turnos abiertos",
        description = "Permite consultar si hay turnos abiertos en una caja específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta realizada exitosamente",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public ResponseEntity<Boolean> consultarTurnosAbiertos(
            @Parameter(description = "Código de la caja", required = true, example = "CAJ01")
            @PathVariable String codigoCaja) {
        boolean hayTurnosAbiertos = gestionCajaService.consultarTurnosAbiertos(codigoCaja);
        return ResponseEntity.ok(hayTurnosAbiertos);
    }
} 