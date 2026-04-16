namespace IMPULS_Desktop
{
    partial class Empreses
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.dataGridViewEmpreses = new System.Windows.Forms.DataGridView();
            this.btnTancar = new System.Windows.Forms.Button();
            this.btnTornar = new System.Windows.Forms.Button();
            this.btnEliminarCandidat = new System.Windows.Forms.Button();
            this.dataGridViewOfertes = new System.Windows.Forms.DataGridView();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewEmpreses)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewOfertes)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridViewEmpreses
            // 
            this.dataGridViewEmpreses.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewEmpreses.Location = new System.Drawing.Point(12, 12);
            this.dataGridViewEmpreses.Name = "dataGridViewEmpreses";
            this.dataGridViewEmpreses.RowHeadersWidth = 51;
            this.dataGridViewEmpreses.RowTemplate.Height = 24;
            this.dataGridViewEmpreses.Size = new System.Drawing.Size(748, 156);
            this.dataGridViewEmpreses.TabIndex = 2;
            // 
            // btnTancar
            // 
            this.btnTancar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTancar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTancar.Location = new System.Drawing.Point(602, 380);
            this.btnTancar.Name = "btnTancar";
            this.btnTancar.Size = new System.Drawing.Size(158, 47);
            this.btnTancar.TabIndex = 8;
            this.btnTancar.Text = "❌Tancar";
            this.btnTancar.UseVisualStyleBackColor = false;
            this.btnTancar.Click += new System.EventHandler(this.btnTancar_Click);
            // 
            // btnTornar
            // 
            this.btnTornar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTornar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTornar.Location = new System.Drawing.Point(315, 380);
            this.btnTornar.Name = "btnTornar";
            this.btnTornar.Size = new System.Drawing.Size(158, 47);
            this.btnTornar.TabIndex = 9;
            this.btnTornar.Text = "↩Tornar";
            this.btnTornar.UseVisualStyleBackColor = false;
            this.btnTornar.Click += new System.EventHandler(this.btnTornar_Click);
            // 
            // btnEliminarCandidat
            // 
            this.btnEliminarCandidat.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnEliminarCandidat.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnEliminarCandidat.ForeColor = System.Drawing.SystemColors.HighlightText;
            this.btnEliminarCandidat.Location = new System.Drawing.Point(12, 380);
            this.btnEliminarCandidat.Name = "btnEliminarCandidat";
            this.btnEliminarCandidat.Size = new System.Drawing.Size(176, 47);
            this.btnEliminarCandidat.TabIndex = 10;
            this.btnEliminarCandidat.Text = "🗑️Eliminar";
            this.btnEliminarCandidat.UseVisualStyleBackColor = false;
            this.btnEliminarCandidat.Click += new System.EventHandler(this.btnEliminarCandidat_Click);
            // 
            // dataGridViewOfertes
            // 
            this.dataGridViewOfertes.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewOfertes.Location = new System.Drawing.Point(12, 198);
            this.dataGridViewOfertes.Name = "dataGridViewOfertes";
            this.dataGridViewOfertes.RowHeadersWidth = 51;
            this.dataGridViewOfertes.RowTemplate.Height = 24;
            this.dataGridViewOfertes.Size = new System.Drawing.Size(748, 156);
            this.dataGridViewOfertes.TabIndex = 11;
            // 
            // Empreses
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.dataGridViewOfertes);
            this.Controls.Add(this.btnEliminarCandidat);
            this.Controls.Add(this.btnTornar);
            this.Controls.Add(this.btnTancar);
            this.Controls.Add(this.dataGridViewEmpreses);
            this.Name = "Empreses";
            this.Text = "Empreses";
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewEmpreses)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewOfertes)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridViewEmpreses;
        private System.Windows.Forms.Button btnTancar;
        private System.Windows.Forms.Button btnTornar;
        private System.Windows.Forms.Button btnEliminarCandidat;
        private System.Windows.Forms.DataGridView dataGridViewOfertes;
    }
}