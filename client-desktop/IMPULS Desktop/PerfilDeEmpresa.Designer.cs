namespace IMPULS_Desktop
{
    partial class PerfilDeEmpresa
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
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.btnTornar = new System.Windows.Forms.Button();
            this.btnTancar = new System.Windows.Forms.Button();
            this.btnTriarCandidat = new System.Windows.Forms.Button();
            this.btnEliminarCandidat = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridView1
            // 
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Location = new System.Drawing.Point(2, 8);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.RowHeadersWidth = 51;
            this.dataGridView1.RowTemplate.Height = 24;
            this.dataGridView1.Size = new System.Drawing.Size(916, 471);
            this.dataGridView1.TabIndex = 1;
           // 
            // btnTornar
            // 
            this.btnTornar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTornar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTornar.Location = new System.Drawing.Point(516, 513);
            this.btnTornar.Name = "btnTornar";
            this.btnTornar.Size = new System.Drawing.Size(158, 47);
            this.btnTornar.TabIndex = 8;
            this.btnTornar.Text = "↩Tornar";
            this.btnTornar.UseVisualStyleBackColor = false;
            this.btnTornar.Click += new System.EventHandler(this.btnTornar_Click);
            // 
            // btnTancar
            // 
            this.btnTancar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTancar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTancar.Location = new System.Drawing.Point(760, 513);
            this.btnTancar.Name = "btnTancar";
            this.btnTancar.Size = new System.Drawing.Size(158, 47);
            this.btnTancar.TabIndex = 7;
            this.btnTancar.Text = "❌Tancar";
            this.btnTancar.UseVisualStyleBackColor = false;
            this.btnTancar.Click += new System.EventHandler(this.btnTancar_Click);
            // 
            // btnTriarCandidat
            // 
            this.btnTriarCandidat.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTriarCandidat.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTriarCandidat.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTriarCandidat.Location = new System.Drawing.Point(287, 513);
            this.btnTriarCandidat.Name = "btnTriarCandidat";
            this.btnTriarCandidat.Size = new System.Drawing.Size(142, 47);
            this.btnTriarCandidat.TabIndex = 6;
            this.btnTriarCandidat.Text = "Editar";
            this.btnTriarCandidat.UseVisualStyleBackColor = false;
            // 
            // btnEliminarCandidat
            // 
            this.btnEliminarCandidat.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnEliminarCandidat.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnEliminarCandidat.ForeColor = System.Drawing.SystemColors.HighlightText;
            this.btnEliminarCandidat.Location = new System.Drawing.Point(2, 513);
            this.btnEliminarCandidat.Name = "btnEliminarCandidat";
            this.btnEliminarCandidat.Size = new System.Drawing.Size(176, 47);
            this.btnEliminarCandidat.TabIndex = 5;
            this.btnEliminarCandidat.Text = "🗑️Eliminar";
            this.btnEliminarCandidat.UseVisualStyleBackColor = false;
            this.btnEliminarCandidat.Click += new System.EventHandler(this.btnEliminarCandidat_Click);
            // 
            // PerfilDeEmpresa
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(919, 561);
            this.Controls.Add(this.btnTornar);
            this.Controls.Add(this.btnTancar);
            this.Controls.Add(this.btnTriarCandidat);
            this.Controls.Add(this.btnEliminarCandidat);
            this.Controls.Add(this.dataGridView1);
            this.Name = "PerfilDeEmpresa";
            this.Text = "Perfil de l\'empresa";
            this.Load += new System.EventHandler(this.PerfilDeEmpresa_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Button btnTornar;
        private System.Windows.Forms.Button btnTancar;
        private System.Windows.Forms.Button btnTriarCandidat;
        private System.Windows.Forms.Button btnEliminarCandidat;
    }
}