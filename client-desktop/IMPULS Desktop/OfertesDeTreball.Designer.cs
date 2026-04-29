namespace IMPULS_Desktop
{
    partial class OfertesDeTreball
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(OfertesDeTreball));
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.btnEliminar = new System.Windows.Forms.Button();
            this.btnTancar = new System.Windows.Forms.Button();
            this.btnCandidats = new System.Windows.Forms.Button();
            this.btnTornar = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridView1
            // 
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Location = new System.Drawing.Point(3, 81);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.RowHeadersWidth = 51;
            this.dataGridView1.RowTemplate.Height = 24;
            this.dataGridView1.Size = new System.Drawing.Size(1636, 357);
            this.dataGridView1.TabIndex = 0;
            // 
            // btnEliminar
            // 
            this.btnEliminar.BackColor = System.Drawing.SystemColors.Highlight;
            this.btnEliminar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnEliminar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnEliminar.Location = new System.Drawing.Point(3, 465);
            this.btnEliminar.Name = "btnEliminar";
            this.btnEliminar.Size = new System.Drawing.Size(162, 39);
            this.btnEliminar.TabIndex = 2;
            this.btnEliminar.Text = "🗑️Eliminar";
            this.btnEliminar.UseVisualStyleBackColor = false;
            this.btnEliminar.Click += new System.EventHandler(this.btnEliminar_Click);
            // 
            // btnTancar
            // 
            this.btnTancar.BackColor = System.Drawing.SystemColors.Highlight;
            this.btnTancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTancar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTancar.Location = new System.Drawing.Point(749, 465);
            this.btnTancar.Name = "btnTancar";
            this.btnTancar.Size = new System.Drawing.Size(137, 39);
            this.btnTancar.TabIndex = 3;
            this.btnTancar.Text = "❌Tancar";
            this.btnTancar.UseVisualStyleBackColor = false;
            this.btnTancar.Click += new System.EventHandler(this.btnTancar_Click);
            // 
            // btnCandidats
            // 
            this.btnCandidats.BackColor = System.Drawing.SystemColors.Highlight;
            this.btnCandidats.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnCandidats.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnCandidats.Location = new System.Drawing.Point(324, 465);
            this.btnCandidats.Name = "btnCandidats";
            this.btnCandidats.Size = new System.Drawing.Size(184, 39);
            this.btnCandidats.TabIndex = 4;
            this.btnCandidats.Text = "👤Candidats";
            this.btnCandidats.UseVisualStyleBackColor = false;
            this.btnCandidats.Click += new System.EventHandler(this.btnCandidats_Click);
            // 
            // btnTornar
            // 
            this.btnTornar.BackColor = System.Drawing.SystemColors.Highlight;
            this.btnTornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTornar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTornar.Location = new System.Drawing.Point(1172, 465);
            this.btnTornar.Name = "btnTornar";
            this.btnTornar.Size = new System.Drawing.Size(137, 39);
            this.btnTornar.TabIndex = 5;
            this.btnTornar.Text = "↩Tornar";
            this.btnTornar.UseVisualStyleBackColor = false;
            this.btnTornar.Click += new System.EventHandler(this.btnTornar_Click);
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(367, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(689, 69);
            this.label1.TabIndex = 6;
            this.label1.Text = "Ofertes de treball";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.label1.Click += new System.EventHandler(this.label1_Click);
            // 
            // OfertesDeTreball
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
            this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Zoom;
            this.ClientSize = new System.Drawing.Size(1651, 533);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.btnTornar);
            this.Controls.Add(this.btnCandidats);
            this.Controls.Add(this.btnTancar);
            this.Controls.Add(this.btnEliminar);
            this.Controls.Add(this.dataGridView1);
            this.Name = "OfertesDeTreball";
            this.Text = "Ofertes de Treball";
            this.Load += new System.EventHandler(this.OfertesDeTreball_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Button btnEliminar;
        private System.Windows.Forms.Button btnTancar;
        private System.Windows.Forms.Button btnCandidats;
        private System.Windows.Forms.Button btnTornar;
        private System.Windows.Forms.Label label1;
    }
}