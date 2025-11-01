}
}

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan File CSV");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        // Tambahkan ekstensi .csv jika pengguna tidak menambahkannya
        if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
        fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
        }
        try (BufferedWriter writer = new BufferedWriter(new
        FileWriter(fileToSave))) {
        writer.write("ID,Nama,Nomor Telepon,Kategori\n"); // Header CSV
        for (int i = 0; i < model.getRowCount(); i++) {
        writer.write(
        model.getValueAt(i, 0) + "," +
        model.getValueAt(i, 1) + "," +
        model.getValueAt(i, 2) + "," +
        model.getValueAt(i, 3) + "\n"
        );
        }
        JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke " + fileToSave.getAbsolutePath());
        } catch (IOException ex) {
        showError("Gagal menulis file: " + ex.getMessage());
        }
      }
    }
    
    private void importFromCSV() {
        showCSVGuide();
        int confirm = JOptionPane.showConfirmDialog(
        this, "Apakah Anda yakin file CSV yang dipilih sudah sesuai dengan format?", "Konfirmasi Impor CSV",
        JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File CSV");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToOpen = fileChooser.getSelectedFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
        String line = reader.readLine(); // Baca header
        if (!validateCSVHeader(line)) {
        JOptionPane.showMessageDialog(this, "Format header CSV tidak valid. Pastikan header adalah: ID,Nama,Nomor Telepon,Kategori", "Kesalahan CSV", JOptionPane.ERROR_MESSAGE);
        return;
        }
        int rowCount = 0;
        int errorCount = 0;
        int duplicateCount = 0;
        StringBuilder errorLog = new StringBuilder("Baris dengan kesalahan:\n");
        while ((line = reader.readLine()) != null) {
        rowCount++;

        String[] data = line.split(",");

        if (data.length != 4) {
        errorCount++;

        errorLog.append("Baris ").append(rowCount +

        1).append(": Format kolom tidak sesuai.\n");
        continue;
        }
        String nama = data[1].trim();
        String nomorTelepon = data[2].trim();
        String kategori = data[3].trim();
        if (nama.isEmpty() || nomorTelepon.isEmpty()) {
        errorCount++;

        errorLog.append("Baris ").append(rowCount +

        1).append(": Nama atau Nomor Telepon kosong.\n");
        continue;
        }
        if (!validatePhoneNumber(nomorTelepon)) {
        errorCount++;

        errorLog.append("Baris ").append(rowCount +

        1).append(": Nomor Telepon tidak valid.\n");
        continue;
        }
        try {
        if
        (controller.isDuplicatePhoneNumber(nomorTelepon, null)) {
        duplicateCount++;

        errorLog.append("Baris ").append(rowCount +

        1).append(": Kontak sudah ada.\n");
        continue;
        }
        } catch (SQLException ex) {
        Logger.getLogger(PengelolaanKontakFrame.class.getName()).log(Level.SEVERE
        , null, ex);
        }
        try {
        controller.addContact(nama, nomorTelepon,
        kategori);
        } catch (SQLException ex) {
        errorCount++;
        errorLog.append("Baris ").append(rowCount +
        1).append(": Gagal menyimpan ke database - ").append(ex.getMessage()).append("\n");
        }
        }
        loadContacts();
        if (errorCount > 0 || duplicateCount > 0) {
        errorLog.append("\nTotal baris dengan kesalahan: ").append(errorCount).append("\n");
        errorLog.append("Total baris duplikat: ").append(duplicateCount).append("\n");
        JOptionPane.showMessageDialog(this,
        errorLog.toString(), "Kesalahan Impor", JOptionPane.WARNING_MESSAGE);
        } else {
        JOptionPane.showMessageDialog(this, "Semua data berhasil diimpor.");
        }
        } catch (IOException ex) {
        showError("Gagal membaca file: " + ex.getMessage());
        }
        }
        }
    }
    
    private void showCSVGuide() {
        String guideMessage = "Format CSV untuk impor data:\n" +
        "- Header wajib: ID, Nama, Nomor Telepon, Kategori\n" +
        "- ID dapat kosong (akan diisi otomatis)\n" +
        "- Nama dan Nomor Telepon wajib diisi\n" +
        "- Contoh isi file CSV:\n" +
        " 1, Andi, 08123456789, Teman\n" +
        " 2, Budi Doremi, 08567890123, Keluarga\n\n" +
        "Pastikan file CSV sesuai format sebelum melakukan impor.";
        JOptionPane.showMessageDialog(this, guideMessage, "Panduan Format CSV", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean validateCSVHeader(String header) {
        return header != null &&
        header.trim().equalsIgnoreCase("ID,Nama,Nomor Telepon,Kategori");
    }
    
/**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
@@ -363,11 +501,11 @@ public void mouseClicked(java.awt.event.MouseEvent evt) {
}// </editor-fold>//GEN-END:initComponents

private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        exportToCSV();      // TODO add your handling code here:
}//GEN-LAST:event_btnExportActionPerformed

private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        // TODO add your handling code here:
        importFromCSV();        // TODO add your handling code here:
}//GEN-LAST:event_btnImportActionPerformed

private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
