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
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewEmpreses)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewOfertes)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridViewEmpreses
            // 
            this.dataGridViewEmpreses.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridViewEmpreses.AutoSizeRowsMode = System.Windows.Forms.DataGridViewAutoSizeRowsMode.AllCells;
            this.dataGridViewEmpreses.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewEmpreses.Location = new System.Drawing.Point(12, 92);
            this.dataGridViewEmpreses.Name = "dataGridViewEmpreses";
            this.dataGridViewEmpreses.RowHeadersWidth = 51;
            this.dataGridViewEmpreses.RowTemplate.Height = 24;
            this.dataGridViewEmpreses.Size = new System.Drawing.Size(1187, 246);
            this.dataGridViewEmpreses.TabIndex = 2;
            // 
            // btnTancar
            // 
            this.btnTancar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTancar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTancar.Location = new System.Drawing.Point(1027, 676);
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
            this.btnTornar.Location = new System.Drawing.Point(582, 676);
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
            this.btnEliminarCandidat.Location = new System.Drawing.Point(12, 676);
            this.btnEliminarCandidat.Name = "btnEliminarCandidat";
            this.btnEliminarCandidat.Size = new System.Drawing.Size(176, 47);
            this.btnEliminarCandidat.TabIndex = 10;
            this.btnEliminarCandidat.Text = "🗑️Eliminar";
            this.btnEliminarCandidat.UseVisualStyleBackColor = false;
            this.btnEliminarCandidat.Click += new System.EventHandler(this.btnEliminar_Click);
            // 
            // dataGridViewOfertes
            // 
            this.dataGridViewOfertes.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridViewOfertes.AutoSizeRowsMode = System.Windows.Forms.DataGridViewAutoSizeRowsMode.AllCells;
            this.dataGridViewOfertes.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewOfertes.Location = new System.Drawing.Point(12, 455);
            this.dataGridViewOfertes.Name = "dataGridViewOfertes";
            this.dataGridViewOfertes.RowHeadersWidth = 51;
            this.dataGridViewOfertes.RowTemplate.Height = 24;
            this.dataGridViewOfertes.Size = new System.Drawing.Size(1187, 215);
            this.dataGridViewOfertes.TabIndex = 11;
            // 
            // label2
            // 
            this.label2.BackColor = System.Drawing.Color.Transparent;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(276, 9);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(689, 75);
            this.label2.TabIndex = 12;
            this.label2.Text = "Empreses";
            this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(276, 377);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(689, 75);
            this.label1.TabIndex = 13;
            this.label1.Text = "Ofertes";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
    //        this.label1.Click += new System.EventHandler(this.label1_Click);
            // 
            // Empreses
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1211, 735);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.label2);
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
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
    }
}