import java.text.SimpleDateFormat

NEWLINE = System.getProperty("line.separator")

def tableName = (TABLE != null ? TABLE.getName() : "MY_TABLE").toLowerCase()

// Helper function for YAML string escaping
static def escapeYAML(String s) {
    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
}

OUT.append(tableName).append(":").append(NEWLINE)

ROWS.each { row ->
    OUT.append("  - ").append(NEWLINE)

    COLUMNS.each { column ->
        def key = column.name()
        def value = row.value(column)
        def formattedValue

        if (value == null) {
            formattedValue = "null"
        } else if (value instanceof Date) {
            def utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            formattedValue = '"' + utcFormat.format(value) + '"'
        } else if (value instanceof Number || value instanceof Boolean) {
            formattedValue = value.toString()
        } else {
            // Apply enhanced YAML escaping to all string representations
            def strVal = escapeYAML(value.toString())
            formattedValue = '"' + strVal + '"'
        }
        OUT.append("    ").append(key).append(": ").append(formattedValue).append(NEWLINE)
    }
}
