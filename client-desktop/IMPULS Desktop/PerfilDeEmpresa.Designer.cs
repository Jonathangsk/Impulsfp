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
            this.btnEliminarOferta = new System.Windows.Forms.Button();
            this.dataGridView2 = new System.Windows.Forms.DataGridView();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView2)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridView1
            // 
            this.dataGridView1.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Location = new System.Drawing.Point(12, 213);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.RowHeadersWidth = 51;
            this.dataGridView1.RowTemplate.Height = 24;
            this.dataGridView1.Size = new System.Drawing.Size(1730, 80);
            this.dataGridView1.TabIndex = 1;
            // 
            // btnTornar
            // 
            this.btnTornar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTornar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTornar.Location = new System.Drawing.Point(641, 790);
            this.btnTornar.Name = "btnTornar";
            this.btnTornar.Size = new System.Drawing.Size(158, 47);
            this.btnTornar.TabIndex = 8;
            this.btnTornar.Text = "↩Tornar";
            this.btnTornar.UseVisualStyleBackColor = false;
            this.btnTornar.Click += new System.EventHandler(this.btnTornar_Click_1);
            // 
            // btnTancar
            // 
            this.btnTancar.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnTancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTancar.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.btnTancar.Location = new System.Drawing.Point(1158, 790);
            this.btnTancar.Name = "btnTancar";
            this.btnTancar.Size = new System.Drawing.Size(158, 47);
            this.btnTancar.TabIndex = 7;
            this.btnTancar.Text = "❌Tancar";
            this.btnTancar.UseVisualStyleBackColor = false;
            this.btnTancar.Click += new System.EventHandler(this.btnTancar_Click_1);
            // 
            // btnEliminarOferta
            // 
            this.btnEliminarOferta.BackColor = System.Drawing.SystemColors.HotTrack;
            this.btnEliminarOferta.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnEliminarOferta.ForeColor = System.Drawing.SystemColors.HighlightText;
            this.btnEliminarOferta.Location = new System.Drawing.Point(30, 790);
            this.btnEliminarOferta.Name = "btnEliminarOferta";
            this.btnEliminarOferta.Size = new System.Drawing.Size(176, 47);
            this.btnEliminarOferta.TabIndex = 5;
            this.btnEliminarOferta.Text = "🗑️Eliminar";
            this.btnEliminarOferta.UseVisualStyleBackColor = false;
            this.btnEliminarOferta.Click += new System.EventHandler(this.btnEliminar_Click);
            // 
            // dataGridView2
            // 
            this.dataGridView2.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView2.Location = new System.Drawing.Point(30, 523);
            this.dataGridView2.Name = "dataGridView2";
            this.dataGridView2.RowHeadersWidth = 51;
            this.dataGridView2.RowTemplate.Height = 24;
            this.dataGridView2.Size = new System.Drawing.Size(1684, 230);
            this.dataGridView2.TabIndex = 9;
            this.dataGridView2.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridView2_CellContentClick);
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(362, 374);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(689, 75);
            this.label1.TabIndex = 10;
            this.label1.Text = "Ofertes de treball";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // label2
            // 
            this.label2.BackColor = System.Drawing.Color.Transparent;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(379, 70);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(689, 75);
            this.label2.TabIndex = 11;
            this.label2.Text = "Perfil de l\'empresa";
            this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // PerfilDeEmpresa
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1757, 984);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.dataGridView2);
            this.Controls.Add(this.btnTornar);
            this.Controls.Add(this.btnTancar);
            this.Controls.Add(this.btnEliminarOferta);
            this.Controls.Add(this.dataGridView1);
            this.Name = "PerfilDeEmpresa";
            this.Text = "Perfil de l\'empresa";
            this.Load += new System.EventHandler(this.PerfilDeEmpresa_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView2)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Button btnTornar;
        private System.Windows.Forms.Button btnTancar;
        private System.Windows.Forms.Button btnEliminarOferta;
        private System.Windows.Forms.DataGridView dataGridView2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
    }
}