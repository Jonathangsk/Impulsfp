namespace IMPULS_Desktop
{
    partial class Candidats
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
            this.btnEliminarCandidat = new System.Windows.Forms.Button();
            this.btnTriarCandidat = new System.Windows.Forms.Button();
            this.btnTancar = new System.Windows.Forms.Button();
            this.btnTornar = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // dataGridView1
            // 
            this.dataGridView1.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridView1.AutoSizeRowsMode = System.Windows.Forms.DataGridViewAutoSizeRowsMode.DisplayedCells;
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Location = new System.Drawing.Point(12, 116);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.RowHeadersWidth = 51;
            this.dataGridView1.RowTemplate.Height = 24;
            this.dataGridView1.Size = new System.Drawing.Size(1724, 461);
            this.dataGridView1.TabIndex = 0;
            // 
            // btnEliminarCandidat
            // 
            this.btnEliminarCandidat.BackColor = System.Drawing.Color.Red;
            this.btnEliminarCandidat.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnEliminarCandidat.ForeColor = System.Drawing.Color.Black;
            this.btnEliminarCandidat.Location = new System.Drawing.Point(519, 28);
            this.btnEliminarCandidat.Name = "btnEliminarCandidat";
            this.btnEliminarCandidat.Size = new System.Drawing.Size(163, 53);
            this.btnEliminarCandidat.TabIndex = 1;
            this.btnEliminarCandidat.Text = "🗑️Rebutjar";
            this.btnEliminarCandidat.UseVisualStyleBackColor = false;
            this.btnEliminarCandidat.Click += new System.EventHandler(this.btnEliminarCandidat_Click);
            // 
            // btnTriarCandidat
            // 
            this.btnTriarCandidat.BackColor = System.Drawing.Color.Lime;
            this.btnTriarCandidat.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTriarCandidat.ForeColor = System.Drawing.Color.Black;
            this.btnTriarCandidat.Location = new System.Drawing.Point(237, 28);
            this.btnTriarCandidat.Name = "btnTriarCandidat";
            this.btnTriarCandidat.Size = new System.Drawing.Size(161, 47);
            this.btnTriarCandidat.TabIndex = 2;
            this.btnTriarCandidat.Text = "👤Aceptar";
            this.btnTriarCandidat.UseVisualStyleBackColor = false;
            this.btnTriarCandidat.Click += new System.EventHandler(this.btnTriarCandidat_Click);
            // 
            // btnTancar
            // 
            this.btnTancar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTancar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTancar.Location = new System.Drawing.Point(729, 31);
            this.btnTancar.Name = "btnTancar";
            this.btnTancar.Size = new System.Drawing.Size(158, 47);
            this.btnTancar.TabIndex = 3;
            this.btnTancar.Text = "❌Tancar";
            this.btnTancar.UseVisualStyleBackColor = false;
            this.btnTancar.Click += new System.EventHandler(this.btnTancar_Click);
            // 
            // btnTornar
            // 
            this.btnTornar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTornar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTornar.Location = new System.Drawing.Point(41, 28);
            this.btnTornar.Name = "btnTornar";
            this.btnTornar.Size = new System.Drawing.Size(158, 47);
            this.btnTornar.TabIndex = 4;
            this.btnTornar.Text = "↩Tornar";
            this.btnTornar.UseVisualStyleBackColor = false;
            this.btnTornar.Click += new System.EventHandler(this.btnTornar_Click);
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(441, 26);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(689, 69);
            this.label1.TabIndex = 7;
            this.label1.Text = "Candidats";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.label1.Click += new System.EventHandler(this.label1_Click);
            // 
            // groupBox1
            // 
            this.groupBox1.BackColor = System.Drawing.Color.Gainsboro;
            this.groupBox1.Controls.Add(this.btnTriarCandidat);
            this.groupBox1.Controls.Add(this.btnTornar);
            this.groupBox1.Controls.Add(this.btnTancar);
            this.groupBox1.Controls.Add(this.btnEliminarCandidat);
            this.groupBox1.Location = new System.Drawing.Point(304, 597);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(925, 90);
            this.groupBox1.TabIndex = 8;
            this.groupBox1.TabStop = false;
            // 
            // Candidats
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(1664, 680);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.dataGridView1);
            this.Name = "Candidats";
            this.Text = "Candidats";
            this.Load += new System.EventHandler(this.Candidats_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.groupBox1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Button btnEliminarCandidat;
        private System.Windows.Forms.Button btnTriarCandidat;
        private System.Windows.Forms.Button btnTancar;
        private System.Windows.Forms.Button btnTornar;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.GroupBox groupBox1;
    }
}