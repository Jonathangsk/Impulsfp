namespace IMPULS_Desktop
{
    partial class CanviContrasenya
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CanviContrasenya));
            this.label1 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.Tornar = new System.Windows.Forms.Button();
            this.Tancar = new System.Windows.Forms.Button();
            this.restaurar = new System.Windows.Forms.Button();
            this.NovaContrasenyaa = new System.Windows.Forms.TextBox();
            this.confirmaContrasenyaa = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.Contrasenyaactuala = new System.Windows.Forms.TextBox();
            this.ContrasenyaActual = new System.Windows.Forms.Label();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 19.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(194, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(354, 38);
            this.label1.TabIndex = 0;
            this.label1.Text = "Canvi de contrasenya";
            // 
            // groupBox1
            // 
            this.groupBox1.BackColor = System.Drawing.Color.Transparent;
            this.groupBox1.Controls.Add(this.Tornar);
            this.groupBox1.Controls.Add(this.Tancar);
            this.groupBox1.Controls.Add(this.restaurar);
            this.groupBox1.Controls.Add(this.NovaContrasenyaa);
            this.groupBox1.Controls.Add(this.confirmaContrasenyaa);
            this.groupBox1.Controls.Add(this.label4);
            this.groupBox1.Controls.Add(this.label3);
            this.groupBox1.Controls.Add(this.Contrasenyaactuala);
            this.groupBox1.Controls.Add(this.ContrasenyaActual);
            this.groupBox1.Location = new System.Drawing.Point(57, 72);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(709, 394);
            this.groupBox1.TabIndex = 1;
            this.groupBox1.TabStop = false;
            // 
            // Tornar
            // 
            this.Tornar.BackColor = System.Drawing.SystemColors.Highlight;
            this.Tornar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Tornar.ForeColor = System.Drawing.SystemColors.ButtonHighlight;
            this.Tornar.Location = new System.Drawing.Point(270, 266);
            this.Tornar.Name = "Tornar";
            this.Tornar.Size = new System.Drawing.Size(154, 45);
            this.Tornar.TabIndex = 5;
            this.Tornar.Text = "↩Tornar";
            this.Tornar.UseVisualStyleBackColor = false;
            this.Tornar.Click += new System.EventHandler(this.tornar_Click);
            // 
            // Tancar
            // 
            this.Tancar.BackColor = System.Drawing.SystemColors.Highlight;
            this.Tancar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Tancar.ForeColor = System.Drawing.SystemColors.ButtonHighlight;
            this.Tancar.Location = new System.Drawing.Point(496, 266);
            this.Tancar.Name = "Tancar";
            this.Tancar.Size = new System.Drawing.Size(147, 42);
            this.Tancar.TabIndex = 4;
            this.Tancar.Text = "❌Tancar";
            this.Tancar.UseVisualStyleBackColor = false;
            this.Tancar.Click += new System.EventHandler(this.tancar_Click);
            // 
            // restaurar
            // 
            this.restaurar.BackColor = System.Drawing.SystemColors.Highlight;
            this.restaurar.Font = new System.Drawing.Font("Microsoft Sans Serif", 13.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.restaurar.ForeColor = System.Drawing.SystemColors.ButtonHighlight;
            this.restaurar.Location = new System.Drawing.Point(31, 266);
            this.restaurar.Name = "restaurar";
            this.restaurar.Size = new System.Drawing.Size(185, 45);
            this.restaurar.TabIndex = 3;
            this.restaurar.Text = "💾Restaurar";
            this.restaurar.UseVisualStyleBackColor = false;
            this.restaurar.Click += new System.EventHandler(this.restaurar_Click);
            // 
            // NovaContrasenyaa
            // 
            this.NovaContrasenyaa.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.NovaContrasenyaa.Location = new System.Drawing.Point(416, 98);
            this.NovaContrasenyaa.Multiline = true;
            this.NovaContrasenyaa.Name = "NovaContrasenyaa";
            this.NovaContrasenyaa.Size = new System.Drawing.Size(239, 40);
            this.NovaContrasenyaa.TabIndex = 1;
      //      this.NovaContrasenyaa.TextChanged += new System.EventHandler(this.textBox2_TextChanged);
            // 
            // confirmaContrasenyaa
            // 
            this.confirmaContrasenyaa.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.confirmaContrasenyaa.Location = new System.Drawing.Point(416, 168);
            this.confirmaContrasenyaa.Multiline = true;
            this.confirmaContrasenyaa.Name = "confirmaContrasenyaa";
            this.confirmaContrasenyaa.Size = new System.Drawing.Size(239, 45);
            this.confirmaContrasenyaa.TabIndex = 2;
    //        this.confirmaContrasenyaa.TextChanged += new System.EventHandler(this.textBox4_TextChanged);
            // 
            // label4
            // 
            this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label4.Location = new System.Drawing.Point(28, 174);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(413, 39);
            this.label4.TabIndex = 7;
            this.label4.Text = "Confirma Contrasenya:";
            // 
            // label3
            // 
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(28, 101);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(281, 42);
            this.label3.TabIndex = 6;
            this.label3.Text = "Nova Contrasenya:";
     //       this.label3.Click += new System.EventHandler(this.label3_Click);
            // 
            // Contrasenyaactuala
            // 
            this.Contrasenyaactuala.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Contrasenyaactuala.Location = new System.Drawing.Point(416, 18);
            this.Contrasenyaactuala.Multiline = true;
            this.Contrasenyaactuala.Name = "Contrasenyaactuala";
            this.Contrasenyaactuala.Size = new System.Drawing.Size(239, 42);
            this.Contrasenyaactuala.TabIndex = 0;
            // 
            // ContrasenyaActual
            // 
            this.ContrasenyaActual.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.ContrasenyaActual.Location = new System.Drawing.Point(25, 18);
            this.ContrasenyaActual.Name = "ContrasenyaActual";
            this.ContrasenyaActual.Size = new System.Drawing.Size(320, 35);
            this.ContrasenyaActual.TabIndex = 0;
            this.ContrasenyaActual.Text = "Contrasenya actual:";
            // 
            // CanviContrasenya
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
            this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Zoom;
            this.ClientSize = new System.Drawing.Size(799, 492);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.label1);
            this.Name = "CanviContrasenya";
            this.Text = "Canvi Contrasenya";
            this.Load += new System.EventHandler(this.CanviContrasenya_Load);
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox Contrasenyaactuala;
        private System.Windows.Forms.Label ContrasenyaActual;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button restaurar;
        private System.Windows.Forms.TextBox NovaContrasenyaa;
        private System.Windows.Forms.TextBox confirmaContrasenyaa;
        private System.Windows.Forms.Button Tancar;
        private System.Windows.Forms.Button Tornar;
    }
}