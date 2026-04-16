namespace IMPULS_Desktop
{
    partial class EliminarEmpresa
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(EliminarEmpresa));
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.textContrasenya = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.btnTornar = new System.Windows.Forms.Button();
            this.button7 = new System.Windows.Forms.Button();
            this.Tancar = new System.Windows.Forms.Button();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.BackColor = System.Drawing.Color.Transparent;
            this.groupBox1.Controls.Add(this.Tancar);
            this.groupBox1.Controls.Add(this.textContrasenya);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this.btnTornar);
            this.groupBox1.Controls.Add(this.button7);
            this.groupBox1.Location = new System.Drawing.Point(88, 58);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(624, 335);
            this.groupBox1.TabIndex = 1;
            this.groupBox1.TabStop = false;
            // 
            // textContrasenya
            // 
            this.textContrasenya.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.textContrasenya.Location = new System.Drawing.Point(124, 177);
            this.textContrasenya.Multiline = true;
            this.textContrasenya.Name = "textContrasenya";
            this.textContrasenya.Size = new System.Drawing.Size(285, 44);
            this.textContrasenya.TabIndex = 48;
            this.textContrasenya.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.textContrasenya.TextChanged += new System.EventHandler(this.textUsuari_TextChanged);
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(60, 88);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(424, 52);
            this.label1.TabIndex = 30;
            this.label1.Text = "Escriu la seva contrasenya";
            // 
            // btnTornar
            // 
            this.btnTornar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(128)))), ((int)(((byte)(0)))));
            this.btnTornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnTornar.ForeColor = System.Drawing.SystemColors.Control;
            this.btnTornar.Location = new System.Drawing.Point(219, 245);
            this.btnTornar.Name = "btnTornar";
            this.btnTornar.Size = new System.Drawing.Size(125, 40);
            this.btnTornar.TabIndex = 29;
            this.btnTornar.Text = " ↩Tornar";
            this.btnTornar.UseVisualStyleBackColor = false;
            this.btnTornar.Click += new System.EventHandler(this.btnTornar_Click);
            // 
            // button7
            // 
            this.button7.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(128)))), ((int)(((byte)(0)))));
            this.button7.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.button7.ForeColor = System.Drawing.SystemColors.Control;
            this.button7.Location = new System.Drawing.Point(26, 245);
            this.button7.Name = "button7";
            this.button7.Size = new System.Drawing.Size(113, 40);
            this.button7.TabIndex = 28;
            this.button7.Text = "✅Aceptar";
            this.button7.UseVisualStyleBackColor = false;
            this.button7.Click += new System.EventHandler(this.button7_Click);
            // 
            // Tancar
            // 
            this.Tancar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(128)))), ((int)(((byte)(0)))));
            this.Tancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Tancar.ForeColor = System.Drawing.SystemColors.Control;
            this.Tancar.Location = new System.Drawing.Point(423, 245);
            this.Tancar.Name = "Tancar";
            this.Tancar.Size = new System.Drawing.Size(103, 40);
            this.Tancar.TabIndex = 49;
            this.Tancar.Text = "❌Tancar";
            this.Tancar.UseVisualStyleBackColor = false;
            this.Tancar.Click += new System.EventHandler(this.tancar_Click);
            // 
            // EliminarEmpresa
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
            this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.groupBox1);
            this.Name = "EliminarEmpresa";
            this.Text = "EliminarEmpresa";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.TextBox textContrasenya;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button btnTornar;
        private System.Windows.Forms.Button button7;
        private System.Windows.Forms.Button Tancar;
    }
}