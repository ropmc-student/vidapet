import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Scanner;

public class VidaPet {

    private static final int DURACAO_CONSULTA_MINUTOS = 30;
    private static final LocalTime HORARIO_ABERTURA = LocalTime.of(8, 0);
    private static final LocalTime ULTIMO_HORARIO_INICIO = LocalTime.of(17, 30);

    public static void main(String[] args) {
        System.out.println("=== Clínica Veterinária VidaPet ===");
        System.out.println("-- Agendamento de consulta --");

        try (Scanner scanner = new Scanner(System.in)) {
            agendarConsulta(scanner);
        }
    }

    private static void agendarConsulta(Scanner scanner) {
        LocalDate dataConsulta = lerData(scanner);
        LocalTime horaInicio = lerHora(scanner, dataConsulta);
        String veterinario = lerCrmv(scanner);
        String animal = lerTextoObrigatorio(scanner, "-- Digite o nome do animal --", "O nome do animal não pode ficar vazio.");

        imprimirConsulta(dataConsulta, horaInicio, veterinario, animal);
    }

    // --- Data ---
    private static LocalDate lerData(Scanner scanner) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            System.out.println("-- Digite a data da consulta (dd/MM/aaaa) --");
            String entrada = scanner.nextLine().trim();

            try {
                LocalDate data = LocalDate.parse(entrada, formato);
                if (data.isBefore(LocalDate.now())) {
                    System.out.println("Data inválida. Deve ser hoje ou uma data futura.");
                    continue;
                }
                return data;
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/aaaa (ex: 25/12/2026).");
            }
        }
    }

    // --- Hora ---
    private static LocalTime lerHora(Scanner scanner, LocalDate dataConsulta) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("H:mm");

        while (true) {
            System.out.println("-- Digite o horário de início da consulta (ex: 14:30) --");
            String entrada = scanner.nextLine().trim();

            try {
                LocalTime hora = LocalTime.parse(entrada, formato);
                if (hora.isBefore(HORARIO_ABERTURA) || hora.isAfter(ULTIMO_HORARIO_INICIO)) {
                    System.out.println("Horário inválido. Deve ser entre 08:00 e 17:30.");
                    continue;
                }
                if (dataConsulta.equals(LocalDate.now()) && !hora.isAfter(LocalTime.now())) {
                    System.out.println("Horário inválido. Esse horário de hoje já passou.");
                    continue;
                }
                return hora;
            } catch (DateTimeParseException e) {
                System.out.println("Horário inválido. Use o formato HH:mm (ex: 14:30).");
            }
        }
    }

    // --- Veterinário ---
    private static String lerCrmv(Scanner scanner) {
        while (true) {
            String crmv = lerTextoObrigatorio(scanner, "-- Digite o CRMV do veterinário (apenas números) --", "O CRMV não pode ficar vazio.");
            if (crmv.matches("\\d+")) {
                return crmv;
            }
            System.out.println("CRMV inválido. Digite apenas números.");
        }
    }

    // --- Leitura genérica de texto não vazio ---
    private static String lerTextoObrigatorio(Scanner scanner, String mensagem, String mensagemErro) {
        while (true) {
            System.out.println(mensagem);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println(mensagemErro);
        }
    }

    // --- Resumo ---
    private static void imprimirConsulta(LocalDate data, LocalTime horaInicio, String veterinario, String animal) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.forLanguageTag("pt-BR"));
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaFim = horaInicio.plusMinutes(DURACAO_CONSULTA_MINUTOS);

        System.out.println();
        System.out.println("=== Consulta agendada ===");
        System.out.printf("Animal:       %s%n", animal);
        System.out.printf("Veterinário:  CRMV %s%n", veterinario);
        System.out.printf("Data:         %s%n", data.format(formatoData));
        System.out.printf("Horário:      %s às %s (%d min)%n",
        horaInicio.format(formatoHora), horaFim.format(formatoHora), DURACAO_CONSULTA_MINUTOS);
        System.out.println("=========================");
    }
}
