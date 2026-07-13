const express = require('express');
const nodemailer = require('nodemailer');

const router = express.Router();
router.use(express.json());

const CONTACTO_DESTINO = process.env.CONTACTO_DESTINO || 'jesusmartinez2103@gmail.com';

function emailValido(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
}

// Escapa HTML para que el contenido del formulario no pueda inyectar
// etiquetas o scripts dentro del correo (nunca insertar texto de usuario crudo en HTML).
function escapeHtml(texto) {
    return String(texto)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function construirHtml({ nombre, email, asunto, mensaje }) {
    const mensajeHtml = escapeHtml(mensaje).replace(/\n/g, '<br>');

    return `
    <div style="background:#0f1222; padding:32px 16px; font-family:Arial,Helvetica,sans-serif;">
      <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="max-width:520px; margin:0 auto; background:#1a1e35; border-radius:16px; overflow:hidden; border:1px solid #2a2f4a;">
        <tr>
          <td style="background:linear-gradient(135deg,#E05A2B,#c9432f); padding:24px 28px;">
            <span style="color:#fff; font-size:20px; font-weight:800;">Donaton</span>
            <div style="color:rgba(255,255,255,0.85); font-size:13px; margin-top:2px;">Nuevo mensaje desde el formulario de contacto</div>
          </td>
        </tr>
        <tr>
          <td style="padding:28px;">
            <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="margin-bottom:20px;">
              <tr>
                <td style="padding:6px 0; color:#8b90ad; font-size:12px; text-transform:uppercase; letter-spacing:0.05em;">Nombre</td>
              </tr>
              <tr>
                <td style="padding:0 0 14px; color:#fff; font-size:15px; font-weight:600;">${escapeHtml(nombre)}</td>
              </tr>
              <tr>
                <td style="padding:6px 0; color:#8b90ad; font-size:12px; text-transform:uppercase; letter-spacing:0.05em;">Correo</td>
              </tr>
              <tr>
                <td style="padding:0 0 14px;">
                  <a href="mailto:${escapeHtml(email)}" style="color:#E05A2B; font-size:15px; text-decoration:none;">${escapeHtml(email)}</a>
                </td>
              </tr>
              ${asunto?.trim() ? `
              <tr>
                <td style="padding:6px 0; color:#8b90ad; font-size:12px; text-transform:uppercase; letter-spacing:0.05em;">Asunto</td>
              </tr>
              <tr>
                <td style="padding:0 0 14px; color:#fff; font-size:15px;">${escapeHtml(asunto)}</td>
              </tr>` : ''}
            </table>

            <div style="border-top:1px solid #2a2f4a; padding-top:18px;">
              <div style="color:#8b90ad; font-size:12px; text-transform:uppercase; letter-spacing:0.05em; margin-bottom:8px;">Mensaje</div>
              <div style="color:#e4e6f1; font-size:14px; line-height:1.6;">${mensajeHtml}</div>
            </div>
          </td>
        </tr>
        <tr>
          <td style="background:#141728; padding:16px 28px; color:#6a6f8f; font-size:11px; text-align:center;">
            Este mensaje fue enviado automáticamente desde el formulario de contacto de Donaton.
          </td>
        </tr>
      </table>
    </div>`;
}

router.post('/', async (req, res) => {
    try {
        const { nombre, email, asunto, mensaje } = req.body;

        if (!nombre?.trim() || !emailValido(email || '') || !mensaje?.trim()) {
            return res.status(400).json({ error: 'Completa nombre, un correo válido y el mensaje.' });
        }

        const transporter = nodemailer.createTransport({
            service: 'gmail',
            auth: {
                user: process.env.EMAIL_USER,
                pass: process.env.EMAIL_PASS,
            },
        });

        await transporter.sendMail({
            from: `"Donaton — Contacto" <${process.env.EMAIL_USER}>`,
            to: CONTACTO_DESTINO,
            replyTo: email,
            subject: asunto?.trim() ? `[Donaton] ${asunto}` : '[Donaton] Nuevo mensaje de contacto',
            text: `Nombre: ${nombre}\nCorreo: ${email}\n\nMensaje:\n${mensaje}`,
            html: construirHtml({ nombre, email, asunto, mensaje }),
        });

        res.json({ mensaje: 'Correo enviado correctamente' });
    } catch (error) {
        console.error('Error enviando correo de contacto:', error.message);
        res.status(500).json({ error: 'No se pudo enviar el mensaje' });
    }
});

module.exports = router;
